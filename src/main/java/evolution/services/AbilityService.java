package evolution.services;

import evolution.dto.AbilityDto;
import evolution.entity.Ability;
import evolution.entity.User;

import java.util.List;

public interface AbilityService {

    void initialise();

    List<Ability> getDefaultBoughtAbilities();

    List<Ability> getDefaultAvailableAbilities();

    List<Ability> getNewUserAbilities();

    void mutate(Long abilityId, User user);

    List<AbilityDto> getAllAvailable(User user);

    List<AbilityDto> getAllBought(User user);

    List<AbilityDto> getAllMutate(User user);

    List<AbilityDto> getAllGame(User user);

    void buy(Long abilityId, User user);
}
