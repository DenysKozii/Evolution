package evolution.controllers.rest;

import evolution.dto.LobbyDto;
import evolution.entity.User;
import evolution.services.LobbyService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/lobby")
public class LobbyRestController {
    private final LobbyService lobbyService;

    @GetMapping
    public LobbyDto findLobby(@AuthenticationPrincipal User user) {
        return lobbyService.findLobby(user);
    }
}
