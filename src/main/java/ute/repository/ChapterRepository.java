package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.dto.response.ChapterResponse;
import ute.entity.Chapter;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
    @Query("SELECT new ute.dto.response.ChapterResponse(c.id, c.title, c.pushlishDate, c.viewCount, c.chapterNumber, c.book.id) " +
            "FROM Chapter c  WHERE c.book.id = :bookID")
    List<ChapterResponse> getChapterByBook(@Param("bookID") Integer bookID);


}
