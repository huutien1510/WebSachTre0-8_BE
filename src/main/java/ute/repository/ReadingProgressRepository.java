// src/main/java/ute/repository/ReadingProgressRepository.java
package ute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.entity.ReadingProgress;

import java.time.LocalDate;
import java.util.Optional;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Integer> {
    Optional<ReadingProgress> findByAccountIdAndReadingDate(Integer accountId, LocalDate readingDate);
}