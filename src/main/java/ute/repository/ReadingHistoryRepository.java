package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ute.dto.response.ReadingHistoryResponse;
import ute.entity.Chapter;

import java.util.List;

@Repository
public interface ReadingHistoryRepository extends JpaRepository<Chapter,Integer> {
    @Query("SELECT new ute.dto.response.ReadingHistoryResponse(c.id,c.chapterNumber,c.book.id,c.book.name,c.book.thumbnail) " +
            "FROM Account a JOIN a.chaptersReadingHistory c " +
            "WHERE a.id= :accountID")
    List<ReadingHistoryResponse> getReadingHistoryByAccount(@Param("accountID") Integer accountID);
}
