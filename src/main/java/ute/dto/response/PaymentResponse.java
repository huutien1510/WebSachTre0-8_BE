package ute.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private String paymentMethod;
    private String paymentUrl; // URL thanh toán cho MoMo, null nếu COD
}