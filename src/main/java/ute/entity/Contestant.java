package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contestant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_contest")
    @ToString.Exclude
    @JsonManagedReference
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "fk_account")
    @ToString.Exclude
    @JsonManagedReference
    private Account account;


    @Column(columnDefinition = "nvarchar(100)")
    private String submissionName;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String submission;
    @Column(columnDefinition = "date")
    private Date submit_time;
    @Column
    private Integer score;
}
