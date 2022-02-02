package evolution.services.impl;

import evolution.dto.GameDto;
import evolution.entity.Game;
import evolution.entity.Lobby;
import evolution.entity.Unit;
import evolution.entity.User;
import evolution.enums.GameStatus;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.GameMapper;
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
    public GameDto startNewGame(User user) {
        Lobby lobby = lobbyRepository.findByUsers(user).orElseThrow(()->new EntityNotFoundException(""));
        Game game;
        if (lobby.getGame() != null) {
            game = lobby.getGame();
        } else {
            game = new Game();
            game.setGameStatus(GameStatus.RUNNING);
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
    public void freeData() {
        List<Game> games = gameRepository.findAll();
        gameRepository.deleteAll(games);
    }

    @Override
    public GameDto getGame(User user) {
        Lobby lobby = lobbyRepository.findByUsers(user).orElseThrow(()->new EntityNotFoundException(""));
        Game game = lobby.getGame();
        List<User> users = lobby.getUsers().stream()
                .filter(u -> u.getUnits().stream().anyMatch(o -> o.getHp() > 0))
                .collect(Collectors.toList());
        if (users.size() == 1 && lobby.getUsers().size() > 1){
            game.setGameStatus(GameStatus.COMPLETED);
            game.setWinner(users.get(0));
            gameRepository.save(game);
            List<Unit> unitsToRemove = new ArrayList<>();
            for (User u: lobby.getUsers()) {
                unitsToRemove.addAll(u.getUnits());
                u.setGameAbilities(null);
                u.setMutatedAbilities(null);
            }
            lobbyRepository.delete(user.getLobby());
            userRepository.saveAll(lobby.getUsers());
            unitRepository.deleteAll(unitsToRemove);
        }
        return GameMapper.INSTANCE.mapToDto(game);
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