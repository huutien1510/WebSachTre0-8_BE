package ute.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ute.enums.PointType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "point_transaction")
public class PointTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    
    private Integer points;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50)")
    private PointType type;
    
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}