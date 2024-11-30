package ute.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ute.entity.Account;
import ute.entity.Discount;
import ute.entity.OrderDetail;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderReponse {
    private Integer id;
    private Float totalPrice;
    private String address;
    private Date date;
    private String paymentMethod;
    private String status;
    private List<OrderDetailResponse> orderDetails;
    private Integer accountID;
    private String accountName;
    private Integer discountID;
}
