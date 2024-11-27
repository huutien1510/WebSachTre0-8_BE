package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
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
    private Integer id;

    @Column(columnDefinition = "nvarchar(10)")
    private String code;
    @Column(columnDefinition = "nvarchar(20)")
    private String type;
    @Column
    private DecimalFormat value;
    @Column(columnDefinition = "date")
    private LocalDateTime start_date;
    @Column(columnDefinition = "date")
    private LocalDateTime end_date;
    @Column
    private Integer quantity;

    @OneToMany(mappedBy = "discount")
    private List<Orders> orders;
}
