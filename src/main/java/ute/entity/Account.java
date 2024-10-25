package ute.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    private Integer bonus_point;
    @Column(columnDefinition = "varchar(50)")
    private String avatar;
    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL)
    private Cart carts;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<CheckinHistory> checkinHistory;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Rating> ratings;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Comment> comments;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Order> orders;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Contestant> contestants;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Suggestion> suggestions;




}
