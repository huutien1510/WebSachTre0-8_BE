package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Float total_price;
    @Column(columnDefinition = "nvarchar(200)")
    private String address;
    @Column(columnDefinition = "date")
    private LocalDateTime date;
    @Column(columnDefinition = "nvarchar(20)")
    private String payment_method;
    @Column(columnDefinition = "nvarchar(20)")
    private String status;
    private List<OrderDetail> orderDetails;
    @ManyToOne
    @JoinColumn(name = "fk_account")
    private Account account;
}
