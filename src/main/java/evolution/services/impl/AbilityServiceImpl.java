package evolution.services.impl;

import evolution.dto.AbilityDto;
import evolution.dto.UserDto;
import evolution.entity.Ability;
import evolution.entity.User;
import evolution.enums.AbilityType;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.AbilityMapper;
import evolution.repositories.AbilityRepository;
import evolution.repositories.UserRepository;
import evolution.services.AbilityService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AbilityServiceImpl implements AbilityService {

    private final AbilityRepository abilityRepository;
    private final UserRepository userRepository;

    @Value("${defaultBoughtAmount}")
    private Integer defaultBoughtAmount;
    @Value("${defaultNewUserAmount}")
    private Integer defaultNewUserAmount;

    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void initialise() {
        if (abilityRepository.count() == 0) {
            Ability division = createAbility("Ускорить деление", "Уменьшает интервал деления с 6 до 4 секунд", 0, 0, AbilityType.DIVISION, null);
            Ability fang = createAbility("Растут клыки", "Выше урон", 0, 0, AbilityType.FANG, null);
            createAbility("Хитиновый панцирь", "Больше здоровья", 0, 0, AbilityType.SHIELD, null);
            createAbility("Дальше распознает жертву", "Больше радиус распознавания", 0, 0, AbilityType.HUNTING, fang);
            createAbility("Двойное деление", "При делении появляется не 1, а 2 круга", 0, 0, AbilityType.DOUBLE_DIVISION, division);
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
    public List<Ability> getDefaultBoughtAbilities() {
        return abilityRepository.findAll().stream().limit(defaultBoughtAmount).collect(Collectors.toList());
    }

    @Override
    public List<Ability> getDefaultAvailableAbilities() {
        return abilityRepository.findAll().stream().skip(defaultNewUserAmount).collect(Collectors.toList());
    }

    @Override
    public List<Ability> getNewUserAbilities() {
        return abilityRepository.findAll().stream().limit(defaultNewUserAmount).collect(Collectors.toList());
    }

    @Override
    public List<AbilityDto> getAllAvailable(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username " + userDto.getUsername() + " doesn't exists!"));
        return abilityRepository.findAllByAvailableUsers(user).stream()
                .map(AbilityMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AbilityDto> getAllBought(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username " + userDto.getUsername() + " doesn't exists!"));
        return abilityRepository.findAllByBoughtUsers(user).stream()
                .map(AbilityMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AbilityDto> getAllMutate(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username " + userDto.getUsername() + " doesn't exists!"));
        return abilityRepository.findAllByMutatedUsers(user).stream()
                .map(AbilityMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AbilityDto> getAllGame(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username " + userDto.getUsername() + " doesn't exists!"));
        return abilityRepository.findAllByGameUsers(user).stream()
                .map(AbilityMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void buy(Long abilityId, UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username " + userDto.getUsername() + " doesn't exists!"));
        Ability ability = abilityRepository.findById(abilityId).orElseThrow(() -> new EntityNotFoundException(""));
        user.getBoughtAbilities().add(ability);
        user.getAvailableAbilities().remove(ability);
        user.setCoins(user.getCoins() - ability.getCoins());
        user.setCrystals(user.getCrystals() - ability.getCrystals());
        userRepository.save(user);
    }

    @Override
    public void mutate(Long abilityId, UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username " + userDto.getUsername() + " doesn't exists!"));
        Ability ability = abilityRepository.findById(abilityId).orElseThrow(() -> new EntityNotFoundException(""));
        user.getMutatedAbilities().add(ability);
        user.getGameAbilities().remove(ability);
        user.getGameAbilities().addAll(abilityRepository.findAllByConditionAbility(ability));
        userRepository.save(user);
    }

}
