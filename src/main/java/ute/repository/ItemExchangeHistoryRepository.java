package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.entity.ItemExchangeHistory;

public interface ItemExchangeHistoryRepository extends JpaRepository<ItemExchangeHistory, Integer> {
}