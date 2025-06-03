package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    @Query("SELECT o FROM Orders o WHERE o.account.id= :accountID")
    Page<Orders> getOrderByAccount(@Param("accountID") Integer accountID, Pageable pageable);

    @Query("SELECT o FROM Orders o ORDER BY o.date DESC")
    Page<Orders> findAll(Pageable pageable);

    @Query("SELECT SUM(o.totalPrice) FROM Orders o")
    Long totalPrice();

    @Query("SELECT SUM(o.totalPrice) FROM Orders o WHERE o.date >= :start AND o.date < :end")
    Double getDailyRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT MONTH(o.date) as month, SUM(o.totalPrice) as total FROM Orders o WHERE YEAR(o.date) = :year GROUP BY MONTH(o.date) ORDER BY month")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);

    @Query("SELECT YEAR(o.date) as year, SUM(o.totalPrice) as total FROM Orders o GROUP BY YEAR(o.date) ORDER BY year")
    List<Object[]> getYearlyRevenue();
}
