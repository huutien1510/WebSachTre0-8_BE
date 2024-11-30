package ute.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ute.entity.Account;
import ute.entity.Book;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingResponse {
    private Integer id;
    private Float star;
    private String content;
    private Date postDate;
    private Integer bookID;
    private String bookName;
    private Integer accountID;
    private String accountName;
    private String accountAvt;
}
