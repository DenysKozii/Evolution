package evolution.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import evolution.entity.Lobby;
import evolution.entity.Skin;
import evolution.entity.User;

import java.util.Optional;

public interface SkinRepository extends JpaRepository<Skin, Long>, PagingAndSortingRepository<Skin, Long> {

}
