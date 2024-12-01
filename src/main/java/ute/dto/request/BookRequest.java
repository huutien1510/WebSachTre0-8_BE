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
    private String name;
    private String author;
    private String description;
    private List<Genre> genres;
    private String thumbnail;
    private Integer price;

}
