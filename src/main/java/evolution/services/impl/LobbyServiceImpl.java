package evolution.services.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class LobbyServiceImpl implements LobbyService {

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;


    @Override
    public LobbyDto findLobby(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        Lobby lobby;
        Optional<Lobby> lobbyByUser = lobbyRepository.findByUsers(user);
        if (!lobbyByUser.isPresent()) {
            Optional<Lobby> lobbyByRating = lobbyRepository.findFirstByCloseRating(user.getRating());
            if (lobbyByRating.isPresent()) {
                lobby = lobbyByRating.get();
                user.setLobby(lobby);
                lobby.setFilled(true);
            } else {
                lobby = new Lobby();
                lobby.setRating(user.getRating());
                user.setLobby(lobby);
            }
            lobbyRepository.save(lobby);
            userRepository.save(user);
        } else {
            lobby = lobbyByUser.get();
        }
        return LobbyMapper.INSTANCE.mapToDto(lobby);
    }
}
