package evolution.services.impl;

import evolution.dto.UserDto;
import evolution.entity.Role;
import evolution.entity.User;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.UserMapper;
import evolution.repositories.UserRepository;
import evolution.services.AbilityService;
import evolution.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AbilityService abilityService;

    @Override
    public void register(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setRole(Role.USER);
            user.setUsername(username);
            user.setRating(0);
            user.setCoins(100);
            user.setCrystals(10);
            user.setAvailableAbilities(abilityService.getDefaultAvailableAbilities());
            user.setBoughtAbilities(abilityService.getNewUserAbilities());
            userRepository.save(user);
        }
    }

    @Override
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " doesn't exists!"));
        return UserMapper.INSTANCE.mapToDto(user);
    }
}
