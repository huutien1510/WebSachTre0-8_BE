package ute.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSessionResponse {
    private Integer sessionId;
    private Integer chapterId;
    private Integer bookId;
    private LocalDateTime startTime;
    private Integer totalReadingMinutes;
    private Integer pointsEarned;
    private Boolean isActive;
}
