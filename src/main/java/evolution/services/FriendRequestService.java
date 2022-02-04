package evolution.services;

import evolution.dto.FriendRequestDto;
import evolution.dto.UserDto;

import java.util.List;

public interface FriendRequestService {

    boolean invite(UserDto user, String friendUsername, Integer code);

    void accept(UserDto user, Long requestId);

    void reject(UserDto user, Long requestId);

    List<FriendRequestDto> inviteList(UserDto user);

    List<FriendRequestDto> acceptList(UserDto user);

    void remove(UserDto user, String friendUsername, Integer code);
}
