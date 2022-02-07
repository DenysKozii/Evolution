package evolution.services.impl;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evolution.dto.BoxDto;
import evolution.dto.UserDto;
import evolution.entity.Box;
import evolution.entity.User;
import evolution.enums.BoxType;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.BoxMapper;
import evolution.repositories.BoxRepository;
import evolution.repositories.UserRepository;
import evolution.services.BoxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class BoxServiceImpl implements BoxService {

    private final BoxRepository  boxRepository;
    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void initialise() {
        List<Box> boxes = setupBoxesList();
        if (boxRepository.count() != boxes.size()) {
            boxes.stream()
                 .filter(box -> !boxRepository.existsByType(box.getType()))
                 .forEach(boxRepository::save);
        }
    }

    private List<Box> setupBoxesList() {
        Box PLASMA_MULTIPLIER = createBox("PLASMA_MULTIPLIER", BoxType.PLASMA_MULTIPLIER);
        Box DNA_MULTIPLIER = createBox("DNA_MULTIPLIER", BoxType.DNA_MULTIPLIER);
        Box ABILITY_CREDIT = createBox("ABILITY_CREDIT", BoxType.ABILITY_CREDIT);
        Box RATING_DEFENCE = createBox("RATING_DEFENCE", BoxType.RATING_DEFENCE);
        Box SKIN_DISCOUNT = createBox("SKIN_DISCOUNT", BoxType.SKIN_DISCOUNT);
        return Arrays.asList(PLASMA_MULTIPLIER, DNA_MULTIPLIER, ABILITY_CREDIT, RATING_DEFENCE, SKIN_DISCOUNT);
    }

    private Box createBox(String description, BoxType type) {
        Box box = new Box();
        box.setDescription(description);
        box.setType(type);
        return box;
    }

    @Override
    public List<BoxDto> findAll(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                                  .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        return boxRepository.findAllByUsers(user).stream()
                            .map(BoxMapper.INSTANCE::mapToDto)
                            .collect(Collectors.toList());
    }
}
