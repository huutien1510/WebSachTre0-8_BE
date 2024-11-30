package ute.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadingHistoryResponse {
    private Integer chapterID;
    private Integer chapterNumber;
    private Integer bookID;
    private String bookName;
    private String thumbnail;
}
