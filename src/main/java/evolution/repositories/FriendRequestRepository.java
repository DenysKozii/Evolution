package evolution.repositories;

import evolution.entity.FriendRequest;
import evolution.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long>, PagingAndSortingRepository<FriendRequest, Long> {

    List<FriendRequest> findAllByInvitorEmail(String email);

    List<FriendRequest> findAllByAcceptorEmail(String email);

}
