package evolution.services;


import evolution.dto.LobbyDto;
import evolution.entity.User;

public interface LobbyService {

    LobbyDto findLobby(User user);

}
