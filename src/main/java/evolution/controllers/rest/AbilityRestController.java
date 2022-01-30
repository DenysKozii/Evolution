package evolution.controllers.rest;

import evolution.services.AbilityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/ability")
public class AbilityRestController {
    private final AbilityService abilityService;

    @GetMapping("mutate/{abilityId}")
    public void mutate(@PathVariable Long abilityId) {
        abilityService.mutate(abilityId);
    }

}
