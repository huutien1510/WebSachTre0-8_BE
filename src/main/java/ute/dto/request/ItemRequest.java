package ute.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private String type;
    private String name;
    private Integer point;
    private String link;
    private Integer quantity;
    private Boolean active;
    private Integer discountId;
}