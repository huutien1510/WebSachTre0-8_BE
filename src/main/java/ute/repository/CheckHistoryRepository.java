package ute.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ute.entity.CheckinHistory;

public interface CheckHistoryRepository extends JpaRepository<CheckinHistory, Integer> {
    List<CheckinHistory> findByAccountIdOrderByCheckinDateDesc(Integer accountId);
    boolean existsByAccountIdAndCheckinDate(Integer accountId, LocalDate checkinDate);
}
