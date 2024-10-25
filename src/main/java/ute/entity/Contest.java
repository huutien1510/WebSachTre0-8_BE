package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "nvarchar(100)")
    private String name;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String description;
    @Column(columnDefinition = "date")
    private LocalDateTime start_date;
    @Column(columnDefinition = "date")
    private LocalDateTime end_date;
    @Column
    private Integer contestant_count;
}
