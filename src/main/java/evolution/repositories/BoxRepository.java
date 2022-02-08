package evolution.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import evolution.entity.Box;
import evolution.entity.User;
import evolution.enums.BoxType;

import java.util.List;
import java.util.Optional;

public interface BoxRepository extends JpaRepository<Box, Long>, PagingAndSortingRepository<Box, Long> {

    List<Box> findAllByUsers(User user);

    boolean existsByType(BoxType type);

    Optional<Box> findByType(BoxType type);
}
