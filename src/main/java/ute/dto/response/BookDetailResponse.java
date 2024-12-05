package ute.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ute.entity.Chapter;
import ute.entity.Genre;
import ute.entity.Rating;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDetailResponse {
    private Integer id;
    private String name;
    private String author;
    private String description;
    private List<Genre> genres;
    private String type;
    private String thumbnail;
    private Integer price;
    private Integer quantity;
}
