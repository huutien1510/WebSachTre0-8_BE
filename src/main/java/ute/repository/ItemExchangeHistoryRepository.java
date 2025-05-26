package ute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.entity.ItemExchangeHistory;

public interface ItemExchangeHistoryRepository extends JpaRepository<ItemExchangeHistory, Integer> {
    List<ItemExchangeHistory> findByAccountIdOrderByExchangeDateDesc(Integer accountId);
}