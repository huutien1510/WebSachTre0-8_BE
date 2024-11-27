package ute.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ute.dto.response.BookDetailResponse;
import ute.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    @Override
    @Query("SELECT b FROM Book b WHERE b.is_delete = false")
    Page<Book> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM Book b WHERE LOWER(b.name) COLLATE Latin1_General_CI_AI LIKE LOWER(CONCAT('%', :keyword, '%')) AND b.is_delete = 0", nativeQuery = true)
    Page<Book> getBookByKeyWord(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE b.price = 0 AND b.is_delete=false")
    Page<Book> getFreeBook(Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE b.price > 0 AND b.is_delete=false")
    Page<Book> getFeeBook(Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id = :genreID AND b.is_delete=false")
    Page<Book> getBookByGenres(@Param("genreID") Integer genreID, Pageable pageable);

}
