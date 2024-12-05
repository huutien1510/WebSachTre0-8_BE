package ute.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "nvarchar(100)")
    private String name;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String banner;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String description;
    @Column(columnDefinition = "date")
    private Date start_date;
    @Column(columnDefinition = "date")
    private Date end_date;
    @Column
    private Integer maxParticipants;
    @Column
    private Integer currentParticipants;
    @OneToMany(mappedBy = "contest",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private List<Contestant> contestants;
}
