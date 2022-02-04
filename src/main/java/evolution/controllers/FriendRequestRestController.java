package evolution.controllers;

import evolution.dto.FriendRequestDto;
import evolution.dto.UserDto;
import evolution.services.FriendRequestService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/friend")
public class FriendRequestRestController {

    private FriendRequestService friendRequestService;

    @GetMapping("list/invite")
    public List<FriendRequestDto> inviteList(@AuthenticationPrincipal UserDto user) {
        return friendRequestService.inviteList(user);
    }

    @GetMapping("list/accept")
    public List<FriendRequestDto> acceptList(@AuthenticationPrincipal UserDto user) {
        return friendRequestService.acceptList(user);
    }

    @PostMapping("invite/{friendUsername}/{code}")
    public boolean invite(@AuthenticationPrincipal UserDto user, @PathVariable String friendUsername, @PathVariable Integer code) {
        return friendRequestService.invite(user, friendUsername, code);
    }

    @PostMapping("accept/{requestId}")
    public void accept(@AuthenticationPrincipal UserDto user, @PathVariable Long requestId) {
        friendRequestService.accept(user, requestId);
    }

    @PostMapping("reject/{requestId}")
    public void reject(@AuthenticationPrincipal UserDto user, @PathVariable Long requestId) {
        friendRequestService.reject(user, requestId);
    }

    @PostMapping("remove/{friendUsername}/{code}")
    public void remove(@AuthenticationPrincipal UserDto user, @PathVariable String friendUsername, @PathVariable Integer code) {
        friendRequestService.remove(user, friendUsername, code);
    }

}
