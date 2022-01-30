package evolution.controllers.rest;

import evolution.dto.UserDto;
import evolution.mapper.UserMapper;
import evolution.services.AuthorizationService;
import evolution.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserRestController {
    private final AuthorizationService authorizationService;
    private final UserService userService;

    @GetMapping("/")
    public String message(Principal principal) {
        System.out.println("Hi "+principal.getName()+" welcome to SpringCloudOauth2ExampleApplication");
        return "profile";
    }

    @PostMapping
    public void addUser(@Valid UserDto user) {
        userService.addUser(user);
    }

    @GetMapping("profile")
    public UserDto profile() {
        return UserMapper.INSTANCE.mapToDto(authorizationService.getProfileOfCurrent());
    }
}
