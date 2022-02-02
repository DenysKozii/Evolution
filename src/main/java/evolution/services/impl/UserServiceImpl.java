package evolution.services.impl;

import evolution.dto.UserDto;
import evolution.entity.User;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.UserMapper;
import evolution.repositories.UserRepository;
import evolution.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserDto getUser(User currentUser) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(()->new EntityNotFoundException(""));
        return UserMapper.INSTANCE.mapToDto(user);
    }

}
