package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "nvarchar(50)")
    private String type;
    @Column(columnDefinition = "nvarchar(100)")
    private String title;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;
    @Column
    private Boolean seen;

    @ManyToOne
    @JoinColumn(name = "fk_account")
    private Account account;
}
