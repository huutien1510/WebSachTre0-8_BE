package ute.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    private Integer bookID;
    private String bookName;
    private String bookType;
    private String bookThumbnail;
    private Integer quantity;
}
