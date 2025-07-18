package ute.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.dto.request.ExtractedBookInfo;
import ute.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE b.is_delete = false")
    Page<Book> findAll(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.id = :bookID AND b.is_delete = false")
    Optional<Book> findById(@Param("bookID") Integer bookID);

    @Query("SELECT count(b) FROM Book b WHERE b.is_delete = false")
    Long countBook();

    @Query(value = "SELECT * FROM book b WHERE LOWER(b.name) COLLATE utf8_general_ci LIKE LOWER(CONCAT('%', :keyword, '%')) AND b.is_delete = 0", nativeQuery = true)
    Page<Book> getBookByKeyWord(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE b.price = 0 AND b.is_delete=false")
    Page<Book> getFreeBook(Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE b.price > 0 AND b.is_delete=false")
    Page<Book> getFeeBook(Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id = :genreID AND b.is_delete=false")
    Page<Book> getBookByGenres(@Param("genreID") Integer genreID, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "JOIN b.genres g " +
            "WHERE ( g.name = :category " +
            "OR b.name = :bookTitle " +
            "OR b.price BETWEEN :priceMin AND :priceMax " +
            "OR b.description LIKE %:description% " +
            "OR b.author LIKE %:author% )" +
            "AND b.is_delete = false")
    List<Book> getBookByExtractInfo(@Param("priceMin")  Integer priceMin,
                                    @Param("priceMax") Integer priceMax,
                                    @Param("category")  String category,
                                    @Param("bookTitle") String bookTitle,
                                    @Param("author")  String author,
                                    @Param("description") String description);

}
