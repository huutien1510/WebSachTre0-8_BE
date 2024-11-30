package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.dto.response.RatingResponse;
import ute.entity.Rating;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Integer> {

    @Query("SELECT new ute.dto.response.RatingResponse(c.id,c.star,c.content,c.postDate,c.book.id, c.book.name,c.account.id,c.account.name,c.account.avatar) " +
            "FROM Rating c")
    List<RatingResponse> getAllRatings();

    @Query("SELECT new ute.dto.response.RatingResponse(c.id,c.star,c.content,c.postDate,c.book.id, c.book.name,c.account.id,c.account.name,c.account.avatar) " +
            "FROM Rating c " +
            "WHERE c.book.id= :bookID")
    List<RatingResponse> getRatingByBook(@Param("bookID") Integer bookID);

}
