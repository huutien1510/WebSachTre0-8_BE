package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.dto.response.CommentResponse;
import ute.dto.response.RatingResponse;
import ute.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query("SELECT new ute.dto.response.CommentResponse(c.id,c.content,c.postDate,c.chapter.id ,c.account.id,c.account.name,c.account.avatar) " +
            "FROM Comment c " +
            "WHERE c.chapter.id= :chapterID " +
            "ORDER BY c.postDate DESC")
    List<CommentResponse> getCommentByChapter(@Param("chapterID") Integer chapterID);
}
