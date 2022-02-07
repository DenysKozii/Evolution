package evolution.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evolution.dto.LobbyDto;
import evolution.dto.UserDto;
import evolution.entity.Lobby;
import evolution.entity.User;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.LobbyMapper;
import evolution.repositories.LobbyRepository;
import evolution.repositories.UserRepository;
import evolution.services.LobbyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class LobbyServiceImpl implements LobbyService {

    private final        LobbyRepository lobbyRepository;
    private final        UserRepository  userRepository;
    private static final Integer         RATE_DECREASE_COEFFICIENT   = 11;
    private static final Integer         RATE_INCREASE_COEFFICIENT   = 11;
    private static final Integer         PLASMA_INCREASE_COEFFICIENT = 5;
    private static final Double          DNA_INCREASE_COEFFICIENT    = 1.5;

    @Override
    public LobbyDto get(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                                  .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
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
        lobby.getUsers().stream()
             .filter(u -> !u.getId().equals(winner.getId()))
             .forEach(u -> u.setRating(Math.max(0, u.getRating() - RATE_DECREASE_COEFFICIENT)));
        winner.setRating(winner.getRating() + lobby.getUsers().size() * RATE_INCREASE_COEFFICIENT);
        winner.setPlasma(winner.getPlasma() + lobby.getUsers().size() * PLASMA_INCREASE_COEFFICIENT);
        winner.setDna(winner.getDna() + (int) (Math.random() * DNA_INCREASE_COEFFICIENT));
        userRepository.saveAll(lobby.getUsers());
        lobbyRepository.delete(lobby);
    }
}
