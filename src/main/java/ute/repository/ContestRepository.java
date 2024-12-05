package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ute.entity.Contest;

@Repository
public interface ContestRepository extends JpaRepository<Contest,Integer> {

}
