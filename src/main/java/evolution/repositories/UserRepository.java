package evolution.repositories;

import evolution.entity.Game;
import evolution.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, PagingAndSortingRepository<User, String> {
    Optional<User> findByUsername(String username);

    List<User> findAllByGame(Game game);
}
