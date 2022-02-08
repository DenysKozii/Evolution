package evolution.services;


import evolution.dto.SkinDto;
import evolution.dto.UserDto;

import java.util.List;

public interface SkinService {

    List<SkinDto> findAll();

    List<SkinDto> getAllAvailable(UserDto user);

    List<SkinDto> getAllBought(UserDto user);

    void buy(UserDto user, Long skinId);
}
