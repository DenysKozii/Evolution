package evolution.controllers.rest;

import evolution.dto.GameDto;
import evolution.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/game")
public class GameRestController {
    private final GameService gameService;

    @GetMapping("new/{lobbyId}")
    public GameDto newGame(@PathVariable Long lobbyId) {
        return gameService.startNewGame(lobbyId);
    }

    @GetMapping
    public GameDto getGame() {
        return gameService.getGame();
    }
}
