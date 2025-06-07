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
    private String type; 

    @Column(columnDefinition = "nvarchar(100)")
    private String name;

    @Column
    private Integer point;

    @Column(columnDefinition = "nvarchar(255)")
    private String link; 

    @Column
    private Integer quantity; 

    @Column(name = "active")
    private Boolean active; 

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @ManyToMany(mappedBy = "items")
    private List<Account> accounts;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemExchangeHistory> itemExchangeHistories;
}