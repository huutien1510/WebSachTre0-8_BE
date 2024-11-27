package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ute.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Integer> {
}
