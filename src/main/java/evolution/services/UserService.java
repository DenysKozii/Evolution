package evolution.services;

import evolution.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void register(String email, String username);

    List<UserDto> getFriends(UserDto user);
}
