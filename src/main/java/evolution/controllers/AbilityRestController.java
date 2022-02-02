package evolution.controllers;

import evolution.entity.User;
import evolution.services.AbilityService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/ability")
public class AbilityRestController {
    private final AbilityService abilityService;

    @PostMapping("mutate/{abilityId}")
    public void mutate(@AuthenticationPrincipal User user, @PathVariable Long abilityId) {
        abilityService.mutate(abilityId, user);
    }

}
