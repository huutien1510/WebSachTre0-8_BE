package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "fk_account")
    private Account account;
    @Column(columnDefinition = "nvarchar(50)")
    private String type;
    @Column(columnDefinition = "nvarchar(100)")
    private String title;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;
    @Column
    private Boolean seen;
}
