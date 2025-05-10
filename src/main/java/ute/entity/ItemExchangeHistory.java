package ute.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "item_exchange_history")
public class ItemExchangeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    private Integer pointUsed;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date exchangeDate;

    @Column(columnDefinition = "nvarchar(50)")
    private String status; // "SUCCESS", "FAILED", "CANCELLED", ...
}