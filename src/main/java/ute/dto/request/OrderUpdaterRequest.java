package ute.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderUpdaterRequest {
    private Float totalPrice;
    private String address;
    private String paymentMethod;
    private String status;
}
