package evolution.controllers;

import evolution.services.AbilityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/ability")
public class AbilityController {
    private final AbilityService abilityService;

    @GetMapping("mutate/{abilityId}")
    public String mutate(@PathVariable Long abilityId, Model model) {
        abilityService.mutate(abilityId);
        return "redirect:/game";
    }

}
