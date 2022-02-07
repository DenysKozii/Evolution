package evolution.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import evolution.dto.BoxDto;
import evolution.dto.UserDto;
import evolution.services.BoxService;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/boxes")
public class BoxRestController {

    private final BoxService boxService;

    @GetMapping
    public List<BoxDto> findAll(@AuthenticationPrincipal UserDto user) {
        return boxService.findAll(user);
    }

    @PutMapping("{boxId}")
    public void activate(@AuthenticationPrincipal UserDto user, @PathVariable Long boxId){
        boxService.activate(user, boxId);
    }
}