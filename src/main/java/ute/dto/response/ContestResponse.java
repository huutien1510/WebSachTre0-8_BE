package ute.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContestResponse {
    private Integer id;
    private String name;
    private String banner;
    private String description;
    private Date start_date;
    private Date end_date;
    private Integer maxParticipants;
    private Integer currentParticipants;
}
