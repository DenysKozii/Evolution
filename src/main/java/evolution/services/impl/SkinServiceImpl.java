package evolution.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evolution.dto.SkinDto;
import evolution.dto.UserDto;
import evolution.entity.User;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.SkinMapper;
import evolution.repositories.SkinRepository;
import evolution.repositories.UserRepository;
import evolution.services.SkinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SkinServiceImpl implements SkinService {

    private final SkinRepository skinRepository;
    private final UserRepository userRepository;


    @Override
    public List<SkinDto> findAll() {
        return skinRepository.findAll().stream()
                             .map(SkinMapper.INSTANCE::mapToDto)
                             .collect(Collectors.toList());
    }

    @Override
    public List<SkinDto> getAllAvailable(UserDto user) {
        return null;
    }

    @Override
    public List<SkinDto> getAllBought(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                                  .orElseThrow(() -> new EntityNotFoundException("User with id " + userDto.getId() + " doesn't exists!"));
        return user.getSkins().stream()
                             .map(SkinMapper.INSTANCE::mapToDto)
                             .collect(Collectors.toList());
    }

    @Override
    public void buy(UserDto user, Long skinId) {

    }
}
