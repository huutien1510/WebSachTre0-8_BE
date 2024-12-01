package ute.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "nvarchar(100)")
    private String name;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String description;

    @ManyToMany(mappedBy = "genres",cascade ={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @JsonBackReference
    private List<Book> books;

}
