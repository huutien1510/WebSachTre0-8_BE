package ute.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "nvarchar(50)")
    private String name;
    @Column(columnDefinition = "nvarchar(50)")
    private String author;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "fk_book"),
            inverseJoinColumns = @JoinColumn(name = "fk_genre")
    )
    @ToString.Exclude
    @JsonManagedReference
    private List<Genre> genres;

    @Column(columnDefinition = "nvarchar(20)")
    private String type;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String thumbnail;
    @Column
    private Integer price;
    @Column(columnDefinition = "nvarchar(100)")
    private String keySearch;
    @Column
    private Boolean is_delete;

    @OneToOne(mappedBy = "book")
    private OrderDetail orderDetail;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private List<Chapter> chapters;

    @ManyToMany(mappedBy = "books")
    private List<Cart> carts;

    @ManyToMany(mappedBy = "books")
    private List<Account> accounts;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
