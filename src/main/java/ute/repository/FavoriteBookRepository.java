package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.dto.response.BookDetailResponse;
import ute.entity.Book;

import java.util.List;

@Repository
public interface FavoriteBookRepository extends JpaRepository<Book,Integer> {
    @Query("SELECT c.favBooks FROM Account c WHERE c.id= :accountID")
    List<Book> getFavoriteBookByAccount(@Param("accountID") Integer accountID);
}
