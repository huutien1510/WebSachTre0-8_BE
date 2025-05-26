package ute.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemExchangeHistoryResponse {
    private Integer id;
    private String itemName;
    private String itemType;
    private Integer pointUsed;
    private String itemImage;
    private LocalDateTime exchangeDate;
    private String status;
}