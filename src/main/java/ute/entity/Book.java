package ute.entity;

import com.fasterxml.jackson.annotation.*;
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "fk_book"),
            inverseJoinColumns = @JoinColumn(name = "fk_genre")
    )
    @ToString.Exclude
    @JsonManagedReference
    private List<Genre> genres;

    @Column(columnDefinition = "nvarchar(20)")
    private String type = "Sach mem";
    @Column(columnDefinition = "nvarchar(MAX)")
    private String thumbnail;
    @Column
    private Integer price;
    @Column(columnDefinition = "nvarchar(100)")
    private String keySearch;
    @Column
    private Boolean is_delete;
    @Column
    private Integer quantity = 0;

    @OneToMany(mappedBy = "book")
    @ToString.Exclude
    @JsonBackReference
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonManagedReference
    private List<Chapter> chapters;

    @ManyToMany(mappedBy = "books")
    private List<Cart> carts;

    @ManyToMany(mappedBy = "favBooks")
    private List<Account> accounts;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
