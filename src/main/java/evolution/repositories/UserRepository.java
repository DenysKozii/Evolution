package evolution.repositories;

import evolution.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, PagingAndSortingRepository<User, String> {
}
