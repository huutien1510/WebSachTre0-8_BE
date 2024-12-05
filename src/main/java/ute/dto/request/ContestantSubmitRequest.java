package ute.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContestantSubmitRequest {
    private Integer contestantID;
    private String submissionName;
    private String submission;
    private Date submit_time;
}
