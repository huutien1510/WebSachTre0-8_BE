package ute.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RequestDiscount {
    String code;
    String type;
    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal value;
    LocalDate startDate;
    LocalDate endDate;
    Integer quantity;
}
