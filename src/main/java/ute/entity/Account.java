package ute.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    public Integer id;
    @Column(columnDefinition = "varchar(50)")
    private String email;
    @Column(columnDefinition = "varchar(255)")
    private String password;
    @Column(columnDefinition = "nvarchar(50)")
    private String username;
    @Column(columnDefinition = "nvarchar(50)")
    private String name ;
    @Column(columnDefinition = "varchar(11)")
    private String phone;
    @Column(columnDefinition = "nvarchar(50)")
    private String sex;
    @Column(columnDefinition = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date birthday;
    @Column
    private Boolean is_active ;
    @Column
    private Boolean is_admin  ;
    @ElementCollection
    private Set<String> roles;
    @Column
    private Integer bonusPoint;
    @Column
    private Boolean is_deleted;
    @Column(columnDefinition = "varchar(255)")
    private String avatar ;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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
    @ToString.Exclude
    @JsonBackReference
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

    @ManyToMany
    @JoinTable(
            name = "reading_history",
            joinColumns = @JoinColumn(name = "fk_account"),
            inverseJoinColumns = @JoinColumn(name = "fk_chapter")
    )
    private List<Chapter> chaptersReadingHistory;



}
