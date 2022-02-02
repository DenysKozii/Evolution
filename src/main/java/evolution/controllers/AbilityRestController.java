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

    @GetMapping("available")
    public List<AbilityDto> allAvailableAbilities(@AuthenticationPrincipal User user) {
        return abilityService.getAllAvailable(user);
    }
    @GetMapping("bought")
    public List<AbilityDto> allBoughtAbilities(@AuthenticationPrincipal User user) {
        return abilityService.getAllBought(user);
    }

    @GetMapping("mutate")
    public List<AbilityDto> allAbilitiesToMutate(@AuthenticationPrincipal User user) {
        return abilityService.getAllMutate(user);
    }

    @GetMapping("game")
    public List<AbilityDto> allAbilitiesToMutateAvailable(@AuthenticationPrincipal User user) {
        return abilityService.getAllGame(user);
    }

    @PostMapping("mutate/{abilityId}")
    public void mutateAbility(@AuthenticationPrincipal User user, @PathVariable Long abilityId) {
        abilityService.mutate(abilityId, user);
    }

    @PostMapping("buy/{abilityId}")
    public void buyAbility(@AuthenticationPrincipal User user, @PathVariable Long abilityId) {
        abilityService.buy(abilityId, user);
    }

}
