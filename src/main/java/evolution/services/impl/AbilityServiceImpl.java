package evolution.services.impl;

import evolution.entity.Ability;
import evolution.entity.User;
import evolution.enums.AbilityType;
import evolution.exception.EntityNotFoundException;
import evolution.repositories.AbilityRepository;
import evolution.repositories.UserRepository;
import evolution.services.AbilityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void initialise() {
        if (abilityRepository.count() == 0) {
            Ability division = createAbility("Ускорить деление", "Уменьшает интервал деления с 6 до 4 секунд",0, 0, AbilityType.DIVISION, null);
            Ability fang = createAbility("Растут клыки", "Выше урон",0, 0, AbilityType.FANG, null);
            createAbility("Хитиновый панцирь","Больше здоровья",0, 0, AbilityType.SHIELD, null);
            createAbility("Дальше распознает жертву","Больше радиус распознавания",0, 0, AbilityType.HUNTING, fang);
            createAbility("Двойное деление","При делении появляется не 1, а 2 круга",0, 0, AbilityType.DOUBLE_DIVISION, division);
        }
    }

    private Ability createAbility(String title, String description, Integer coins, Integer crystals, AbilityType type, Ability ability) {
        Ability newAbility = new Ability();
        newAbility.setTitle(title);
        newAbility.setDescription(description);
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
    public void mutate(Long abilityId, User user) {
        Ability ability = abilityRepository.findById(abilityId).orElseThrow(()->new EntityNotFoundException(""));
        user.getMutatedAbilities().add(ability);
        user.getGameAbilities().remove(ability);
        user.getGameAbilities().addAll(abilityRepository.findAllByConditionAbility(ability));
        userRepository.save(user);
    }

}
