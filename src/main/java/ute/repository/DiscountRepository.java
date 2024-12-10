package ute.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ute.entity.Discount;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    Optional<Discount> findByCode(String code);
    @Query("SELECT d FROM Discount d")
    Page<Discount> findAll(Pageable pageable);
    boolean existsByCode(String code);
}
