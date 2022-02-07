package evolution.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evolution.dto.BoxDto;
import evolution.dto.GameDto;
import evolution.dto.LobbyDto;
import evolution.dto.UserDto;
import evolution.entity.Game;
import evolution.entity.Lobby;
import evolution.entity.Unit;
import evolution.entity.User;
import evolution.enums.GameStatus;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.BoxMapper;
import evolution.mapper.GameMapper;
import evolution.mapper.LobbyMapper;
import evolution.repositories.BoxRepository;
import evolution.repositories.GameRepository;
import evolution.repositories.LobbyRepository;
import evolution.repositories.UnitRepository;
import evolution.repositories.UserRepository;
import evolution.services.AbilityService;
import evolution.services.BoxService;
import evolution.services.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
