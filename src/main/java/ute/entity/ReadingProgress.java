package ute.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "reading_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadingProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    LocalDate readingDate;
    Integer totalSeconds; // Tổng số giây đã đọc trong ngày
    Integer totalPoints;  // Tổng điểm đã cộng trong ngày
}