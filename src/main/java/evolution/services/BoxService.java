package evolution.services;


import evolution.dto.BoxDto;
import evolution.dto.LobbyDto;
import evolution.dto.UserDto;

import java.util.List;

public interface BoxService {

    void initialise();

    List<BoxDto> findAll(UserDto user);

}
