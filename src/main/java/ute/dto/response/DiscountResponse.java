package ute.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountResponse {
    Integer id;
    String code;
    String type;
    BigDecimal value;
    LocalDate startDate;
    LocalDate endDate;
    Integer quantity;
}
