package evolution.services.impl;

import com.google.common.collect.ImmutableList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
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
        List<Ability> abilities = setupAbilitiesList();
        if (abilityRepository.count() != abilities.size()) {
            abilities.stream()
                     .filter(ability -> !abilityRepository.existsByType(ability.getType()))
                     .forEach(abilityRepository::save);
        }
    }

    private List<Ability> setupAbilitiesList() {
        Ability SPEED_1 = createAbility("Увеличить скорость", "Увеличивает скорость передвижения", 0, 0, AbilityType.SPEED_1, ImmutableList.of());
        Ability DAMAGE_2 = createAbility("Клыки", "Урон +20", 0, 0, AbilityType.DAMAGE_2, ImmutableList.of());
        Ability HEALTH_3 = createAbility("Хитиновый панцирь", "Здоровье взрослого +50", 0, 0, AbilityType.HEALTH_3, ImmutableList.of());
        Ability DIVISION_4 = createAbility("Уменьшить время деления", "Время деления -2 секунды", 0, 0, AbilityType.DIVISION_4, ImmutableList.of());
        Ability RUN_5 = createAbility("Боязнь", "Если здоровья <20%, убегать", 0, 0, AbilityType.RUN_5, ImmutableList.of(SPEED_1));
        Ability TEAM_6 = createAbility("Оберегать семью", "Ходить по парам", 0, 0, AbilityType.TEAM_6, ImmutableList.of(SPEED_1));
        Ability HUNT_7 = createAbility("Охота", "Нападать на цель со здоровьем <50%", 0, 0, AbilityType.HUNT_7, ImmutableList.of(DAMAGE_2));
        Ability DAMAGE_FULL_8 = createAbility("Ярость", "Урон +40, если >90% здоровья", 0, 0, AbilityType.DAMAGE_FULL_8, ImmutableList.of(DAMAGE_2));
        Ability DAMAGE_9 = createAbility("Рога", "Урон +15, если >30% здоровья", 0, 0, AbilityType.DAMAGE_9, ImmutableList.of(HEALTH_3));
        Ability CHILD_HP_10 = createAbility("Самостоятельность", "Здоровье детеныша +50", 0, 0, AbilityType.CHILD_HP_10, ImmutableList.of(HEALTH_3));
        Ability GROW_11 = createAbility("Ускорить взросление", "Время взросления -3 секунды", 0, 0, AbilityType.GROW_11, ImmutableList.of(DIVISION_4));
        Ability AGING_12 = createAbility("Увеличить Длительность жизни", "Время старения +10 секунд", 0, 0, AbilityType.AGING_12, ImmutableList.of(DIVISION_4));
        Ability HELP_13 = createAbility("Альтруизм", "Бежать к раненому союзнику", 0, 0, AbilityType.HELP_13, ImmutableList.of(RUN_5, TEAM_6));
        Ability BIG_TEAM_14 = createAbility("Командность", "Ходить по 3", 0, 0, AbilityType.BIG_TEAM_14, ImmutableList.of(TEAM_6));
        Ability VAMPIRISM_15 = createAbility("Поедание добычи", "Получать 100 здоровья, если убил цель", 0, 0, AbilityType.VAMPIRISM_15, ImmutableList.of(HUNT_7, DAMAGE_FULL_8));
        Ability DIED_16 = createAbility("Притворяться мертвым", "Притворяться мертвым на 3 секунды, когда здоровья <30%, восполнять 50", 0, 0, AbilityType.DIED_16, ImmutableList.of(DAMAGE_9, CHILD_HP_10));
        Ability STUN_17 = createAbility("Массивные удары", "Если здоровья >200, каждый удар оглушает цель на 2 секунды", 150, 1, AbilityType.STUN_17, ImmutableList.of(CHILD_HP_10));
        Ability PROVOCATION_18 = createAbility("Защищать детей", "Увеличить размер взрослых на 10, быть приоритетом для цели вместо детей на расстоянии 50", 50, 0, AbilityType.PROVOCATION_18, ImmutableList.of(GROW_11, AGING_12));
        Ability CHILD_SPEED_19 = createAbility("Активный рост", "Время взросления -3 секунды, скорость детей +10", 10, 1, AbilityType.CHILD_SPEED_19, ImmutableList.of(GROW_11, AGING_12));
        Ability HELP_SPEED_20 = createAbility("Помогать раненым", "Скорость передвижения к раненому союзнику +15", 150, 0, AbilityType.HELP_SPEED_20, ImmutableList.of(HELP_13));

        return Arrays.asList(SPEED_1, DAMAGE_2, HEALTH_3, DIVISION_4, RUN_5, TEAM_6, HUNT_7, DAMAGE_FULL_8, DAMAGE_9,
                             CHILD_HP_10, GROW_11, AGING_12, HELP_13, BIG_TEAM_14, VAMPIRISM_15, DIED_16, STUN_17,
                             PROVOCATION_18, CHILD_SPEED_19, HELP_SPEED_20);
    }

    private Ability createAbility(String title, String description, Integer plasma, Integer dna, AbilityType type, List<Ability> abilities) {
        Ability newAbility = new Ability();
        newAbility.setTitle(title);
        newAbility.setDescription(description);
        newAbility.setPlasma(plasma);
        newAbility.setDna(dna);
        newAbility.setType(type);
        newAbility.getConditionAbilities().addAll(abilities);
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
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        return abilityRepository.findAllByAvailableUsers(user).stream()
                .map(AbilityMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AbilityDto> getAllBought(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        return abilityRepository.findAllByBoughtUsers(user).stream()
                .map(AbilityMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AbilityDto> getAllMutated(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        return abilityRepository.findAllByMutatedUsers(user).stream()
                .map(AbilityMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AbilityDto> getAllGame(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        return abilityRepository.findAllByGameUsers(user).stream()
                .map(AbilityMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void buy(Long abilityId, UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        Ability ability = abilityRepository.findById(abilityId).orElseThrow(() -> new EntityNotFoundException(""));
        user.getBoughtAbilities().add(ability);
        user.getAvailableAbilities().remove(ability);
        user.setPlasma(user.getPlasma() - ability.getPlasma());
        user.setDna(user.getDna() - ability.getDna());
        userRepository.save(user);
    }

    @Override
    public void mutate(Long abilityId, UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        Ability ability = abilityRepository.findById(abilityId).orElseThrow(() -> new EntityNotFoundException(""));
        user.getMutatedAbilities().add(ability);
        user.getGameAbilities().remove(ability);
        List<Ability> abilities = abilityRepository.findAllByConditionAbilities(ability).stream()
                .filter(a -> user.getGameAbilities()
                        .containsAll(a.getConditionAbilities()))
                .collect(Collectors.toList());
        user.getGameAbilities().addAll(abilities);
        userRepository.save(user);
    }

}
