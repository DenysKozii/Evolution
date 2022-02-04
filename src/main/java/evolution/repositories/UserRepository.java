package evolution.repositories;

import evolution.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    List<User> findAllByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndCode(String username, Integer code);
}
