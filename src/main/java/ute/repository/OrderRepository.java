package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.entity.Orders;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer> {
    @Query("SELECT o FROM Orders o WHERE o.account.id= :accountID")
    List<Orders> getOrderByAccount(@Param("accountID") Integer accountID);

    @Query("SELECT o FROM Orders o")
    List<Orders> getAllOrder();
}
