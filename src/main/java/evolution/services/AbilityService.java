package evolution.services;

import evolution.entity.Ability;
import evolution.entity.User;

import java.util.List;

public interface AbilityService {

    void initialise();

    List<Ability> getStartList();

    void mutate(Long abilityId, User user);
}
