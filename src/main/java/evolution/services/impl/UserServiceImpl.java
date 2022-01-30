package evolution.services.impl;

import evolution.dto.UserDto;
import evolution.entity.Role;
import evolution.entity.User;
import evolution.repositories.UserRepository;
import evolution.services.AbilityService;
import evolution.services.AuthorizationService;
import evolution.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;
    private final AbilityService abilityService;
    private final PasswordEncoder passwordEncoder;

    public boolean addUser(UserDto userDto) {
        Optional<User> userFromDb = userRepository.findByUsername(userDto.getUsername());
        User user;
        if (userFromDb.isPresent()) {
            user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() ->
                    new UsernameNotFoundException("Invalid Credentials"));
        } else {
            user = new User();
            user.setRole(Role.USER);
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getUsername()));
            user.setRating(0);
            user.setCoins(100);
            user.setCrystals(10);
            user.setAvailableAbilities(abilityService.getStartList());
            userRepository.save(user);
        }
        authorizationService.authorizeUser(user);
        return true;
    }

}
