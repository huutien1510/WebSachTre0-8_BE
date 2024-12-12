package ute.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ute.dto.response.OrderDetailResponse;
import ute.entity.Account;
import ute.entity.Discount;
import ute.entity.OrderDetail;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Float totalPrice;
    private String address;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime date;
    private String paymentMethod;
    private Integer account;
    private String discountCode;
    private List<OrderDetailResponse> orderDetails;
}
