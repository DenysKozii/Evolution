package evolution.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evolution.dto.FriendRequestDto;
import evolution.dto.UserDto;
import evolution.entity.FriendRequest;
import evolution.entity.User;
import evolution.exception.EntityNotFoundException;
import evolution.mapper.FriendRequestMapper;
import evolution.repositories.FriendRequestRepository;
import evolution.repositories.UserRepository;
import evolution.services.FriendRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public boolean invite(UserDto user, String friendUsername, Integer code) {
        List<FriendRequestDto> friendRequests = inviteList(user);
        if (friendRequests.isEmpty()) {
            User invitor = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(""));
            User acceptor = userRepository.findByUsernameAndCode(friendUsername, code).orElseThrow(() -> new EntityNotFoundException(""));
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setInvitor(invitor);
            friendRequest.setAcceptor(acceptor);
            friendRequestRepository.save(friendRequest);
            return true;
        }
        return false;
    }

    @Override
    public void accept(UserDto user, Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException(""));
        User invitor = friendRequest.getInvitor();
        User acceptor = friendRequest.getAcceptor();
        invitor.getFriends().add(acceptor);
        acceptor.getFriends().add(invitor);
        userRepository.save(invitor);
        userRepository.save(acceptor);
        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public void reject(UserDto user, Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException(""));
        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public List<FriendRequestDto> inviteList(UserDto user) {
        return friendRequestRepository.findAllByInvitorEmail(user.getEmail()).stream()
                .map(FriendRequestMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendRequestDto> acceptList(UserDto user) {
        return friendRequestRepository.findAllByAcceptorEmail(user.getEmail()).stream()
                .map(FriendRequestMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(UserDto user, String friendUsername, Integer code) {
        User userCurrent = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(""));
        User friend = userRepository.findByUsernameAndCode(friendUsername, code).orElseThrow(() -> new EntityNotFoundException(""));
        userCurrent.getFriends().remove(friend);
        friend.getFriends().remove(userCurrent);
        userRepository.save(friend);
        userRepository.save(userCurrent);
    }
}
