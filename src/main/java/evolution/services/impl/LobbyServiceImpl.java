package evolution.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evolution.dto.LobbyDto;
import evolution.dto.UserDto;
import evolution.entity.Box;
import evolution.entity.Lobby;
import evolution.entity.User;
import evolution.enums.BoxType;
import evolution.enums.RatingStep;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.LobbyMapper;
import evolution.repositories.LobbyRepository;
import evolution.repositories.UserRepository;
import evolution.services.BoxService;
import evolution.services.LobbyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class LobbyServiceImpl implements LobbyService {

    private final        LobbyRepository lobbyRepository;
    private final        UserRepository  userRepository;
    private final        BoxService      boxService;
    private static final Integer         RATING_FOR_STEPS            = 100;
    private static final Integer         RATING_DECREASE_COEFFICIENT = 11;
    private static final Integer         RATING_INCREASE_COEFFICIENT = 11;
    private static final Integer         PLASMA_INCREASE_COEFFICIENT = 5;
    private static final Double          DNA_INCREASE_COEFFICIENT    = 1.5;

    @Override
    public LobbyDto get(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                                  .orElseThrow(() -> new EntityNotFoundException("User with idemail " + userDto.getId() + " doesn't exists!"));
        Lobby lobby;
        Optional<Lobby> lobbyByUser = lobbyRepository.findByUsers(user);

        if (!lobbyByUser.isPresent()) {
            Optional<Lobby> lobbyByRating = lobbyRepository.findFirstByCloseRating(user.getRating());
            if (lobbyByRating.isPresent()) {
                lobby = lobbyByRating.get();
                user.setLobby(lobby);
            } else {
                lobby = new Lobby();
                lobby.setRating(user.getRating());
                user.setLobby(lobby);
                lobby.setHost(user);
            }
            lobby.getUsers().add(user);
            lobbyRepository.save(lobby);
            userRepository.save(user);
        } else {
            lobby = lobbyByUser.get();
        }
        return LobbyMapper.INSTANCE.mapToDto(lobby);
    }

    @Override
    public boolean invite(UserDto userDto, String friendUsername, Integer code) {
        User invitor = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        User acceptor = userRepository.findByUsernameAndCode(friendUsername, code).orElseThrow(() -> new EntityNotFoundException(""));
        Lobby lobby = invitor.getLobby();
        if (lobby == null) {
            lobby = new Lobby();
            invitor.setLobby(lobby);
            lobby.getUsers().add(invitor);
            userRepository.save(invitor);
        }
        if (!lobby.getUsers().contains(acceptor)) {
            lobby.getUsers().add(acceptor);
            acceptor.setLobby(lobby);
            lobbyRepository.save(lobby);
            userRepository.save(acceptor);
            return true;
        }
        return false;
    }

    // todo check all conditions in the ticket
    @Override
    public boolean cancel(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        Lobby lobby = user.getLobby();
        if (lobby == null) {
            return false;
        }
        lobby.setStarted(false);
        lobby.getUsers().remove(user);
        user.setLobby(null);
        userRepository.save(user);
        if (lobby.getUsers().isEmpty()) {
            lobbyRepository.save(lobby);
            lobbyRepository.delete(lobby);
        } else {
            lobbyRepository.save(lobby);
        }
        return true;
    }

    @Override
    public void start(UserDto userDto) {
        User invitor = userRepository.findById(userDto.getId())
                                     .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        Lobby lobby = lobbyRepository.findByUsers(invitor)
                                     .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        lobby.setStarted(true);
        lobbyRepository.save(lobby);
    }

    @Override
    public void complete(UserDto userDto) {
        User winner = userRepository.findById(userDto.getId())
                                    .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        Lobby lobby = lobbyRepository.findByUsers(winner)
                                     .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        for (User user: lobby.getUsers()) {
            if (!user.equals(winner)){
                Box box = boxService.getByType(BoxType.RATING_DEFENCE);
                if (user.getActivatedBoxes().contains(box)){
                    user.getActivatedBoxes().remove(box);
                } else {
                    user.setRating(Math.max(0, user.getRating() - RATING_DECREASE_COEFFICIENT));
                    user.setRatingStep(RatingStep.getByOrder(user.getRating() / RATING_FOR_STEPS));
                }
            }
        }
        winner.setRating(winner.getRating() + lobby.getUsers().size() * RATING_INCREASE_COEFFICIENT);
        winner.setRatingStep(RatingStep.getByOrder(winner.getRating() / RATING_FOR_STEPS));
        if (winner.getRating() / 100 > winner.getMaximumRating() / 100) {
            winner.setMaximumRating(winner.getRating());
            winner.getBoxes().add(boxService.getRandom());
        }
        winner.setPlasma(winner.getPlasma() + lobby.getUsers().size() * PLASMA_INCREASE_COEFFICIENT);
        winner.setDna(winner.getDna() + (int) (Math.random() * DNA_INCREASE_COEFFICIENT));
        userRepository.saveAll(lobby.getUsers());
        lobbyRepository.delete(lobby);
    }
}
