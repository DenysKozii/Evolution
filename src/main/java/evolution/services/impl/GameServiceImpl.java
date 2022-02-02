package evolution.services.impl;

import evolution.dto.GameDto;
import evolution.dto.LobbyDto;
import evolution.entity.Game;
import evolution.entity.Lobby;
import evolution.entity.Unit;
import evolution.entity.User;
import evolution.enums.GameStatus;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.GameMapper;
import evolution.mapper.LobbyMapper;
import evolution.repositories.GameRepository;
import evolution.repositories.LobbyRepository;
import evolution.repositories.UnitRepository;
import evolution.repositories.UserRepository;
import evolution.services.AbilityService;
import evolution.services.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final UnitRepository unitRepository;
    private final AbilityService abilityService;


    @Override
    public GameDto accept(User user) {
        Lobby lobby = lobbyRepository.findByUsers(user).orElseThrow(() -> new EntityNotFoundException(""));
        Game game;
        if (lobby.getGame() != null) {
            game = lobby.getGame();
            game.setAcceptedAmount(game.getAcceptedAmount() + 1);
            if (game.getAcceptedAmount().equals(lobby.getUsers().size())) {
                game.setGameStatus(GameStatus.RUNNING);
            }
        } else {
            game = new Game();
            game.setGameStatus(GameStatus.WAITING);
            lobby.setGame(game);
        }

        double userIndex = lobby.getUsers().indexOf(user);
        Unit unit = newUnit(userIndex * 50 * Math.pow(-1, userIndex), userIndex * 50 * Math.pow(-1, userIndex), 1.0, 100, 50, 10, 40.0);
        unit.setUser(user);
        user.getUnits().add(unit);

        gameRepository.save(game);
        user.setGameAbilities(abilityService.getStartList());
        unitRepository.save(unit);
        userRepository.save(user);
        lobbyRepository.save(lobby);
        return GameMapper.INSTANCE.mapToDto(game);
    }

    @Override
    public void reject(User user) {
        Lobby lobby = lobbyRepository.findByUsers(user).orElseThrow(() -> new EntityNotFoundException(""));
        Game game = lobby.getGame();
        lobby.setGame(null);
        lobbyRepository.save(lobby);
        gameRepository.delete(game);
    }

    @Override
    public LobbyDto getCurrent(User user) {
        Lobby lobby = lobbyRepository.findByUsers(user).orElseThrow(() -> new EntityNotFoundException(""));
        Game game = lobby.getGame();
        List<User> users = lobby.getUsers().stream()
                .filter(u -> u.getUnits().stream().anyMatch(o -> o.getHp() > 0))
                .collect(Collectors.toList());
        LobbyDto lobbyDto = LobbyMapper.INSTANCE.mapToDto(lobby);
        if (users.size() == 1 && lobby.getUsers().size() > 1) {
            game.setGameStatus(GameStatus.COMPLETED);
            User winner = users.get(0);
            game.setWinner(winner);
            gameRepository.save(game);
            List<Unit> unitsToRemove = new ArrayList<>();
            for (User u : lobby.getUsers()) {
                unitsToRemove.addAll(u.getUnits());
                u.setGameAbilities(null);
                u.setMutatedAbilities(null);
                u.setUnits(null);
                u.setLobby(null);
            }
            lobby.getUsers().stream().filter(u -> u.getId().equals(winner.getId())).forEach(u -> u.setRating(Math.max(0, u.getRating() - 10)));
            lobby.setUsers(null);
            winner.setRating(winner.getRating() + 10);
            winner.setCoins(winner.getCoins() + 10);
            lobbyDto = LobbyMapper.INSTANCE.mapToDto(lobby);
            lobbyRepository.delete(lobby);
            userRepository.saveAll(users);
            unitRepository.deleteAll(unitsToRemove);
        }
        return lobbyDto;
    }

    @Override
    public Unit newUnit(Double x, Double y, Double bodyRadius, Integer hp, Integer damage, Integer speed, Double detectRadius) {
        Unit unit = new Unit();
        unit.setAge(0);
        unit.setBodyRadius(bodyRadius);
        unit.setX(x);
        unit.setY(y);
        unit.setHp(hp);
        unit.setDamage(damage);
        unit.setSpeed(speed);
        unit.setDetectRadius(detectRadius);
        return unit;
    }

}
