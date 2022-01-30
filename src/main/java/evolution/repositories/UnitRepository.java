package evolution.repositories;

import evolution.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Long>, PagingAndSortingRepository<Unit, Long> {

    @Query(value = "select * from units where user_id = ?1", nativeQuery = true)
    List<Unit> findAllByUserId(Long userId);

}
