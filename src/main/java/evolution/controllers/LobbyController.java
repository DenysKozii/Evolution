package evolution.controllers;

import evolution.dto.LobbyDto;
import evolution.services.LobbyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/lobby")
public class LobbyController {
    private final LobbyService lobbyService;

    @GetMapping
    public String findLobby(Model model) {
        LobbyDto lobby = lobbyService.findLobby();
        model.addAttribute("lobby", lobby);
        return "lobby";
    }
}
