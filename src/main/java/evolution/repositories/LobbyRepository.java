package evolution.repositories;

import evolution.entity.Lobby;
import evolution.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface LobbyRepository extends JpaRepository<Lobby, Long>, PagingAndSortingRepository<Lobby, Long> {

    Optional<Lobby> findByUsers(User user);

    @Query(value = "select * from lobbies where rating > ?1 - 100 and rating < ?1 + 100", nativeQuery = true)
    Optional<Lobby> findFirstByCloseRating(Integer rating);

}
