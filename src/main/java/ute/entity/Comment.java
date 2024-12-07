package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;
    @Column
    private Date postDate;

    @ManyToOne
    @JoinColumn(name = "fk_chapter")
    @ToString.Exclude
    @JsonManagedReference
    private Chapter chapter;

    @ManyToOne
    @JoinColumn(name="fk_account")
    @ToString.Exclude
    @JsonManagedReference
    private Account account;

}
