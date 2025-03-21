package ute.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CartResponse {
    Integer id;
    String name;
    String author;
    String description;
    String type;
    String thumbnail;
    Integer price;
    Integer quantity;
}
