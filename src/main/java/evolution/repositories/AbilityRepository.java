package evolution.repositories;

import evolution.entity.Ability;
import evolution.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AbilityRepository extends JpaRepository<Ability, Long>, PagingAndSortingRepository<Ability, Long> {

    List<Ability> findAllByMutatedUsers(User user);

    List<Ability> findAllByConditionAbility(Ability conditionAbility);

}
