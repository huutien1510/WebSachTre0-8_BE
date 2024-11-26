package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @ManyToMany
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "fk_book"),
            inverseJoinColumns = @JoinColumn(name = "fk_genre")
    )
    private List<Genre> genres;

    @Column(columnDefinition = "nvarchar(20)")
    private String type;
    @Column(columnDefinition = "nvarchar(50)")
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
    private List<Chapter> chapters;

    @ManyToMany(mappedBy = "books")
    private List<Cart> carts;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
