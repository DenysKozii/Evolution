package evolution.controllers;

import evolution.dto.LobbyDto;
import evolution.dto.UserDto;
import evolution.services.LobbyService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/lobby")
public class LobbyRestController {
    private final LobbyService lobbyService;

    @GetMapping
    public LobbyDto get(@AuthenticationPrincipal UserDto user) {
        return lobbyService.get(user);
    }

    @GetMapping("start")
    public void start(@AuthenticationPrincipal UserDto user) {
        lobbyService.start(user);
    }

    @GetMapping("complete")
    public void complete(@AuthenticationPrincipal UserDto user) {
        lobbyService.complete(user);
    }

    @PostMapping("cancel")
    public boolean cancel(@AuthenticationPrincipal UserDto user) {
        return lobbyService.cancel(user);
    }

    @PostMapping("invite/{friendUsername}/{code}")
    public boolean inviteFriend(@AuthenticationPrincipal UserDto user, @PathVariable String friendUsername, @PathVariable Integer code) {
        return lobbyService.invite(user, friendUsername, code);
    }
}
