package evolution.controllers;

import evolution.dto.UserDto;
import evolution.services.AuthorizationService;
import evolution.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class UserController {
    private final AuthorizationService authorizationService;
    private final UserService userService;

    @PostMapping
    public String addUser(@Valid UserDto user) {
        userService.addUser(user);
        return "redirect:/lobby";
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @GetMapping("profile")
    public String profile(Model model) {
        model.addAttribute("user", authorizationService.getProfileOfCurrent());
        return "profile";
    }
}
