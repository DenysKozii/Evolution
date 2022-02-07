package evolution.services;


import evolution.dto.LobbyDto;
import evolution.dto.UserDto;

public interface LobbyService {

    LobbyDto get(UserDto userDto);

    boolean invite(UserDto userDto, String friendUsername, Integer code);

    boolean cancel(UserDto userDto);

    void start(UserDto userDto);

    void complete(UserDto userDto);
}
