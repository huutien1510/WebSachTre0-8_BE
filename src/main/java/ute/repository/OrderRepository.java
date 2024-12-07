package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer> {
    @Query("SELECT o FROM Orders o WHERE o.account.id= :accountID")
    List<Orders> getOrderByAccount(@Param("accountID") Integer accountID);

    @Query("SELECT o FROM Orders o")
    Page<Orders> findAll(Pageable pageable);
    @Query("SELECT SUM(o.totalPrice) FROM Orders o")
    Long totalPrice ();
}
