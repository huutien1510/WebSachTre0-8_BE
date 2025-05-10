package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "nvarchar(20)")
    private String type; // "voucher", "gift", ...

    @Column(columnDefinition = "nvarchar(100)")
    private String name;

    @Column
    private Integer point; // Số điểm cần để đổi

    @Column(columnDefinition = "nvarchar(100)")
    private String link; // Link voucher hoặc thông tin khác

    @Column
    private Integer quantity; // Số lượng còn lại

    @Column
    private Boolean active; // Trạng thái hoạt động

    @ManyToMany(mappedBy = "items")
    private List<Account> accounts;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemExchangeHistory> itemExchangeHistories;
}