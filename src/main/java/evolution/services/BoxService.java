package evolution.services;


import evolution.dto.BoxDto;
import evolution.dto.LobbyDto;
import evolution.dto.UserDto;
import evolution.entity.Box;
import evolution.enums.BoxType;

import java.util.List;

public interface BoxService {

    void initialise();

    List<BoxDto> findAll(UserDto user);

    Box getRandom();

    void activate(UserDto user, Long boxId);

    Box getByType(BoxType ratingDefence);

    void buy(UserDto user, Long boxId);
}
