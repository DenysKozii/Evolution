package evolution.repositories;

import evolution.entity.Game;
import evolution.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long>, PagingAndSortingRepository<Game, Long> {

    Optional<Game> findById(Long id);

    Game findByUsers(User user);

}
