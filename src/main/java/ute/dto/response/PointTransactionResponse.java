package ute.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ute.enums.PointType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PointTransactionResponse {
    private Integer id;
    private Integer accountId;
    private Integer points;
    private PointType type;
    private String description;
    private LocalDateTime createdAt;
}