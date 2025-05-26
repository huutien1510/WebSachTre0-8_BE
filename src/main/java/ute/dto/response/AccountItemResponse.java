package ute.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountItemResponse {
    private Integer id;
    private Integer itemId;
    private String itemName;
    private String itemType;
    private String itemImage;
    private Integer pointUsed;
    private Integer quantity;
    private Boolean used;
    private String codeVoucher;
    private String voucherType;
    private BigDecimal voucherValue;
    private LocalDate voucherEndDate;
}
