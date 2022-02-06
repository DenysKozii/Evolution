package evolution.services;


import evolution.dto.LobbyDto;
import evolution.dto.UserDto;

public interface LobbyService {

    LobbyDto get(UserDto user);

    boolean invite(UserDto user, String friendUsername, Integer code);

    boolean cancel(UserDto user);

    void start(UserDto user);
}
