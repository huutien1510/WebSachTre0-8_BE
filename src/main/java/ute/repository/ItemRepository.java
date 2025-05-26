package ute.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ute.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("Select d from Item d ")
    Page<Item> findAll(Pageable pageable);

    @Query("Select d from Item d where d.quantity > 0 and d.active = true")
    Page<Item> findAllByActiveTrue(Pageable pageable);
}