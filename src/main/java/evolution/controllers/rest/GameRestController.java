package evolution.controllers.rest;

import evolution.dto.GameDto;
import evolution.entity.User;
import evolution.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/game")
public class GameRestController {
    private final GameService gameService;

    @GetMapping("new")
    public GameDto newGame(@AuthenticationPrincipal User user) {
        return gameService.startNewGame(user);
    }

    @GetMapping
    public GameDto getGame(@AuthenticationPrincipal User user) {
        return gameService.getGame(user);
    }
}
