package ute.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ute.entity.ReadingSession;
@Repository
public interface ReadingSessionRepository extends JpaRepository<ReadingSession, Integer> {
    Optional<ReadingSession> findByAccountIdAndIsActiveTrue(Integer accountId);
    Optional<ReadingSession> findByAccountIdAndChapterIdAndIsActiveTrue(Integer accountId, Integer chapterId);
    
    @Query("SELECT COALESCE(SUM(r.pointsEarned), 0) FROM ReadingSession r " +
           "WHERE r.account.id = :accountId AND r.readingDate = :readingDate")
    Integer sumPointsEarnedByAccountIdAndReadingDate(
        @Param("accountId") Integer accountId, 
        @Param("readingDate") LocalDate readingDate
    );
    
    List<ReadingSession> findByAccountIdAndReadingDate(Integer accountId, LocalDate readingDate);
}
