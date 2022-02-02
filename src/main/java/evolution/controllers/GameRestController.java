package evolution.controllers;

import evolution.dto.GameDto;
import evolution.dto.LobbyDto;
import evolution.entity.User;
import evolution.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/game")
public class GameRestController {
    private final GameService gameService;

    @GetMapping("accept")
    public GameDto acceptGame(@AuthenticationPrincipal User user) {
        return gameService.accept(user);
    }

    @GetMapping("reject")
    public void rejectGame(@AuthenticationPrincipal User user) {
        gameService.reject(user);
    }

    @GetMapping
    public LobbyDto getCurrent(@AuthenticationPrincipal User user) {
        return gameService.getCurrent(user);
    }

}
