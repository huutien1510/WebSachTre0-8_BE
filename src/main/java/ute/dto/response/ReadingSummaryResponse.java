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
public class ReadingSummaryResponse {
    private Integer totalReadingMinutes;
    private Integer pointsEarned;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
