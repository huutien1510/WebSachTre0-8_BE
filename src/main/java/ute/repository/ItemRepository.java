package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}