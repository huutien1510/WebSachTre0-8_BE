package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.dto.response.ChapterContentResponse;
import ute.entity.ChapterContent;

import java.util.List;

@Repository
public interface ChapterContentRepository extends JpaRepository<ChapterContent,Integer> {
    @Query("SELECT NEW ute.dto.response.ChapterContentResponse (t.id, t.content, t.contentNumber, t.chapter.id) " +
            "FROM ChapterContent t " +
            "WHERE t.chapter.id = :chapterID")
    List<ChapterContentResponse> getContentByChapter(@Param("chapterID") Integer chapterID);
}
