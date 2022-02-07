package evolution.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evolution.dto.BoxDto;
import evolution.dto.UserDto;
import evolution.entity.User;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.BoxMapper;
import evolution.repositories.BoxRepository;
import evolution.repositories.UserRepository;
import evolution.services.BoxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;
    private final UserRepository userRepository;

    @Override
    public List<BoxDto> findAll(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                                  .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        return boxRepository.findAllByUsers(user).stream()
                            .map(BoxMapper.INSTANCE::mapToDto)
                            .collect(Collectors.toList());
    }
}
