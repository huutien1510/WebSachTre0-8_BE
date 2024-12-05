package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    Discount findByCode(String code);
}
