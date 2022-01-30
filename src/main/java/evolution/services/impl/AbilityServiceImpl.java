package evolution.services.impl;

import evolution.entity.Ability;
import evolution.entity.User;
import evolution.enums.AbilityType;
import evolution.exception.EntityNotFoundException;
import evolution.repositories.AbilityRepository;
import evolution.repositories.UserRepository;
import evolution.services.AbilityService;
import evolution.services.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AbilityServiceImpl implements AbilityService {

    private final AbilityRepository abilityRepository;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void initialise() {
        if (abilityRepository.count() == 0) {
            Ability division = createAbility("Ускорить деление", 0, 0, AbilityType.DIVISION, null);
            Ability fang = createAbility("Растут клыки",0, 0, AbilityType.FANG, null);
            createAbility("Хитиновый панцирь",0, 0, AbilityType.SHIELD, null);
            createAbility("Дальше распознает жертву",0, 0, AbilityType.HUNTING, fang);
            createAbility("Двойное деление",0, 0, AbilityType.DOUBLE_DIVISION, division);
        }
    }

    private Ability createAbility(String title, Integer coins, Integer crystals, AbilityType type, Ability ability) {
        Ability newAbility = new Ability();
        newAbility.setTitle(title);
        newAbility.setCoins(coins);
        newAbility.setCrystals(crystals);
        newAbility.setType(type);
        newAbility.setConditionAbility(ability);
        abilityRepository.save(newAbility);
        return newAbility;
    }

    @Override
    public List<Ability> getStartList() {
        return abilityRepository.findAll().stream().limit(3).collect(Collectors.toList());
    }

    @Override
    public void mutate(Long abilityId) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
        Ability ability = abilityRepository.findById(abilityId).orElseThrow(()->new EntityNotFoundException(""));
        user.getMutatedAbilities().add(ability);
        user.getGameAbilities().remove(ability);
        user.getGameAbilities().addAll(abilityRepository.findAllByConditionAbility(ability));
        userRepository.save(user);
    }

}
