package evolution.services;

import evolution.dto.UserDto;
import evolution.entity.User;

public interface UserService {

    UserDto getUser(User user);
}
