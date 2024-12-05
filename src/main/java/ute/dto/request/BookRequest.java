package ute.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ute.entity.Genre;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String name;
    private String author;
    private String description;
    private String type;
    private List<Integer> genreIDs;
    private String thumbnail;
    private Integer price;
    private Integer quantity = 0;

}
