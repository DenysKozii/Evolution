package evolution.services;


import evolution.dto.LobbyDto;
import evolution.dto.UserDto;
import evolution.entity.User;

public interface LobbyService {

    LobbyDto findLobby(UserDto user);

    boolean invite(UserDto user, String friendUsername, Integer code);

    boolean cancelLobby(UserDto user);
}
