package evolution.services.impl;

import evolution.dto.LobbyDto;
import evolution.entity.Lobby;
import evolution.entity.User;
import evolution.mapper.LobbyMapper;
import evolution.repositories.LobbyRepository;
import evolution.repositories.UserRepository;
import evolution.services.*;
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
    private final AuthorizationService authorizationService;


    @Override
    public LobbyDto findLobby() {
        User user = authorizationService.getProfileOfCurrent();
        Optional<Lobby> lobbyOptional = lobbyRepository.findFirstByCloseRating(user.getRating());
        Lobby lobby;
        if (!lobbyOptional.isPresent()) {
            lobby = new Lobby();
            lobby.setRating(user.getRating());
            lobby.getUsers().add(user);
            lobbyRepository.save(lobby);
            user.setLobby(lobby);
            userRepository.save(user);
        } else {
            lobby = lobbyOptional.get();
            lobby.getUsers().add(user);
            user.setLobby(lobby);
            lobbyRepository.save(lobby);
            userRepository.save(user);
        }
        return LobbyMapper.INSTANCE.mapToDto(lobby);
    }
}
