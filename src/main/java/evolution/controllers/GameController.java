package evolution.controllers;

import evolution.dto.GameDto;
import evolution.dto.UserDto;
import evolution.mapper.UserMapper;
import evolution.services.AuthorizationService;
import evolution.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final AuthorizationService authorizationService;

    @GetMapping("new/{lobbyId}")
    public String newGame(@PathVariable Long lobbyId, Model model) {
        GameDto game = gameService.startNewGame(lobbyId);
        model.addAttribute("game", game);
        UserDto user = UserMapper.INSTANCE.mapToDto(authorizationService.getProfileOfCurrent());
        model.addAttribute("user", user);
        return "game";
    }

    @GetMapping
    public String getGame(Model model) {
        GameDto game = gameService.getGame();
        game.getUsers().forEach(u->u.setUnits(u.getUnits().stream().filter(o->o.getHp()>0).collect(Collectors.toList())));
        model.addAttribute("game", game);
        UserDto user = UserMapper.INSTANCE.mapToDto(authorizationService.getProfileOfCurrent());
        model.addAttribute("user", user);
        return "game";
    }

    @GetMapping("/free")
    public String freeData() {
        gameService.freeData();
        return "redirect:/gamePattern/list";
    }
}
