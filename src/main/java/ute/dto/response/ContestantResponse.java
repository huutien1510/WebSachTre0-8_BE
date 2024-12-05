package ute.dto.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ute.entity.Account;
import ute.entity.Contest;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContestantResponse {
    private Integer id;
    private Integer contestID;
    private Integer accountID;
    private String accountName;
    private String submissionName;
    private String submission;
    private Date submit_time;
    private Integer score;
}
