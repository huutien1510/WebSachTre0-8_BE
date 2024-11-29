package ute.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(50)")
    private String email;
    @Column(columnDefinition = "varchar(50)")
    private String password;
    @Column(columnDefinition = "nvarchar(50)")
    private String name;
    @Column(columnDefinition = "varchar(11)")
    private String phone;
    @Column
    private Boolean is_active;
    @Column
    private Boolean is_admin;
    @Column
    private Integer bonusPoint;
    @Column(columnDefinition = "varchar(50)")
    private String avatar;

    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL)
    private Cart carts;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<CheckinHistory> checkinHistory;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Rating> ratings;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private List<Comment> comments;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Orders> orders;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Contestant> contestants;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Suggestion> suggestions;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_item",
            joinColumns = @JoinColumn(name = "fk_account"),
            inverseJoinColumns = @JoinColumn(name = "fk_item")
    )
    private List<Item> items;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "favorites_book",
            joinColumns = @JoinColumn(name = "fk_account"),
            inverseJoinColumns = @JoinColumn(name = "fk_book")
    )
    private List<Book> favBooks;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_notification",
            joinColumns = @JoinColumn(name = "fk_account"),
            inverseJoinColumns = @JoinColumn(name = "fk_notification")
    )
    private List<Notification> notifications;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "reading_history",
            joinColumns = @JoinColumn(name = "fk_account"),
            inverseJoinColumns = @JoinColumn(name = "fk_chapter")
    )
    private List<Chapter> chaptersReadingHistory;



}
