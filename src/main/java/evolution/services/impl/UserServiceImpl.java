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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AbilityService abilityService;

    @Override
    public void register(String email, String username) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            Integer usernameCode = userRepository.findAllByUsername(username).size();
            User user = new User();
            user.setRole(Role.USER);
            user.setUsername(username);
            user.setEmail(email);
            user.setCode(usernameCode);
            user.setPeerId(generatePeerId());
            user.setRating(0);
            user.setPlasma(100);
            user.setDna(10);
            user.setAvailableAbilities(abilityService.getDefaultAvailableAbilities());
            user.setBoughtAbilities(abilityService.getNewUserAbilities());
            userRepository.save(user);
        }
    }

    private String generatePeerId(){
        return UUID.randomUUID().toString();
    }

    @Override
    public List<UserDto> getFriends(UserDto user) {
        User current = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User with email " + user.getEmail() + " doesn't exists!"));
        return current.getFriends().stream()
                .map(UserMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " doesn't exists!"));
        return UserMapper.INSTANCE.mapToDto(user);
    }
}
