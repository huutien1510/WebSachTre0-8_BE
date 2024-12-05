package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ute.entity.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    @Query("SELECT c.carts FROM Account c WHERE c.id= :accountID")
    List<Cart> getCartByAccount(@Param("accountID") Integer accountID);
}
