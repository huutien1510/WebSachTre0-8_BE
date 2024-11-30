package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ute.dto.response.ChapterResponse;
import ute.entity.Chapter;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
    @Query("SELECT new ute.dto.response.ChapterResponse(c.id, c.title, c.pushlishDate, c.viewCount, c.chapterNumber, c.book.id) " +
            "FROM Chapter c  WHERE c.book.id = :bookID")
    List<ChapterResponse> getChapterByBook(@Param("bookID") Integer bookID);

    @Transactional
    @Modifying
    @Query("UPDATE Chapter c SET c.viewCount = c.viewCount + 1 WHERE c.id = :chapterID")
    Integer upView(@Param("chapterID") Integer chapterID);

    @Query("SELECT sum(c.viewCount) FROM Chapter c")
    Long getTotalView();
}
