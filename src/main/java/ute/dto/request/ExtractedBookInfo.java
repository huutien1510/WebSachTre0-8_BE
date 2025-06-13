package ute.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtractedBookInfo {

    @JsonProperty("price_min")  // Jackson annotation to map JSON key to Java field
    private Integer priceMin;  // Use Integer to allow null values

    @JsonProperty("price_max")  // Jackson annotation to map JSON key to Java field
    private Integer priceMax;  // Use Integer to allow null values

    private String category;
    private String bookTitle;
    private String description;
    private String author;

    @JsonProperty("has_new_info")  // Jackson annotation to map JSON key to Java field
    private boolean hasNewInfo;
}
