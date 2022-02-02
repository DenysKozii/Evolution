package evolution.controllers;

import evolution.dto.UserDto;
import evolution.entity.User;
import evolution.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserRestController {

    private UserService userService;

    @GetMapping
    public UserDto profile(@AuthenticationPrincipal User user) {
        return userService.getUser(user);
    }

}
