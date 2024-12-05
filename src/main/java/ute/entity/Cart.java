package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "fk_account")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;

    @ManyToMany
    @JoinTable(
            name = "cart_book",
            joinColumns = @JoinColumn(name = "fk_cart"),
            inverseJoinColumns = @JoinColumn(name = "fk_book")
    )
    private List<Book> books;
}
