package ute.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Integer id;
    private String type;
    private String name;
    private Integer point;
    private String link;
    private Integer quantity;
    private Boolean active;
    // Thông tin voucher (nếu có)
    private Integer discountId;
    private String voucherCode;
    private String voucherType;
    private BigDecimal voucherValue;
    private LocalDate voucherEndDate;
}
