package evolution.services;

import evolution.dto.AbilityDto;
import evolution.dto.UserDto;
import evolution.entity.Ability;
import evolution.entity.User;

import java.util.List;

public interface AbilityService {

    void initialise();

    List<Ability> getDefaultBoughtAbilities();

    List<Ability> getDefaultAvailableAbilities();

    List<Ability> getNewUserAbilities();

    void mutate(Long abilityId, UserDto user);

    List<AbilityDto> getAllAvailable(UserDto user);

    List<AbilityDto> getAllBought(UserDto user);

    List<AbilityDto> getAllMutate(UserDto user);

    List<AbilityDto> getAllGame(UserDto user);

    void buy(Long abilityId, UserDto user);
}
