package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
}
