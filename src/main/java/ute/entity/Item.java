package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "nvarchar(20)")
    private String type;
    @Column(columnDefinition = "nvarchar(100)")
    private String name;
    @Column
    private Integer point;
    @Column(columnDefinition = "nvarchar(100)")
    private String link;

    @ManyToMany(mappedBy = "items")
    private List<Account> accounts;
}
