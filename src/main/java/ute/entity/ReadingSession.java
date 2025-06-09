package ute.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reading_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    
    private LocalDateTime startTime;
    private LocalDateTime lastActiveTime;
    private LocalDateTime endTime;
    private Integer totalReadingMinutes;
    private Integer pointsEarned;
    private Boolean isActive;
    private LocalDate readingDate;
    private Boolean warningShown;
    private LocalDateTime lastWarningTime;
}
