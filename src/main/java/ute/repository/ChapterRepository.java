package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.entity.Chapter;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
    @Query("SELECT c FROM Chapter c JOIN FETCH c.book b WHERE b.id= :bookID AND c.chapterNumber= :chapter_number")
    Optional<Chapter> getChapterByBookAndChapterNumber(@Param("bookID") Integer bookID, @Param("chapter_number") Integer chapter_number);
}
