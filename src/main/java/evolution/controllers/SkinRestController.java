package evolution.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import evolution.dto.SkinDto;
import evolution.dto.UserDto;
import evolution.services.SkinService;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/skins")
public class SkinRestController {

    private final SkinService skinService;

    @GetMapping
    public List<SkinDto> getAll() {
        return skinService.findAll();
    }

    @GetMapping("available")
    public List<SkinDto> getAllAvailable(@AuthenticationPrincipal UserDto user) {
        return skinService.getAllAvailable(user);
    }

    @GetMapping("bought")
    public List<SkinDto> getAllBought(@AuthenticationPrincipal UserDto user) {
        return skinService.getAllBought(user);
    }

    @PostMapping("{skinId}")
    public void buy(@AuthenticationPrincipal UserDto user, @PathVariable Long skinId) {
        skinService.buy(user, skinId);
    }

}
