package evolution.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import evolution.entity.Box;
import evolution.entity.User;

import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long>, PagingAndSortingRepository<Box, Long> {

    List<Box> findAllByUsers(User user);

}
