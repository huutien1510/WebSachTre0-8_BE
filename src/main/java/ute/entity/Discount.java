package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "nvarchar(10)", nullable = false, unique = true)
    String code;

    @Column(columnDefinition = "nvarchar(20)", nullable = false)
    String type;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal value;

    @Column(columnDefinition = "date", nullable = false)
    LocalDate startDate;

    @Column(columnDefinition = "date", nullable = false)
    LocalDate endDate;

    @Column(nullable = false)
    Integer quantity;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Orders> orders;
}
