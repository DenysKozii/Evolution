package evolution.controllers;

import evolution.dto.AbilityDto;
import evolution.entity.User;
import evolution.services.AbilityService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/ability")
public class AbilityRestController {
    private final AbilityService abilityService;

    @GetMapping("all")
    public List<AbilityDto> allAbilities(@AuthenticationPrincipal User user) {
        return abilityService.getAll(user);
    }

    @GetMapping("mutate")
    public List<AbilityDto> allAbilitiesToMutate(@AuthenticationPrincipal User user) {
        return abilityService.getAll(user);
    }

    @PostMapping("mutate/{abilityId}")
    public void mutateAbility(@AuthenticationPrincipal User user, @PathVariable Long abilityId) {
        abilityService.mutate(abilityId, user);
    }

    @PostMapping("buy/{abilityId}")
    public void buyAbility(@AuthenticationPrincipal User user, @PathVariable Long abilityId) {
        abilityService.mutate(abilityId, user);
    }

}
