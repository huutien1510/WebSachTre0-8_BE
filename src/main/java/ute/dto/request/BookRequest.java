package ute.dto.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ute.entity.*;

import java.util.List;

//@Getter
//@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    private Integer id;
    private String name;
    private String author;
    private String description;
    private List<Genre> genres;
    private String type;
    private String thumbnail;
    private Integer price;
    private String keySearch;
    private Boolean is_delete;
    private List<Chapter> chapters;
    private List<Rating> ratings;
}
