package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Float totalPrice;
    @Column(columnDefinition = "nvarchar(200)")
    private String address;
    @Column(columnDefinition = "date")
    private LocalDateTime date;
    @Column(columnDefinition = "nvarchar(20)")
    private String paymentMethod;
    @Column(columnDefinition = "nvarchar(20)")
    private String status;

    @OneToMany(mappedBy = "orders")
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "fk_account")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "fk_discount")
    private Discount discount;
}
