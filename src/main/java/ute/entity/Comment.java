package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Chapter chapter;
    @ManyToOne
    @JoinColumn(name="fk_account")
    private Account account;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;
    @Column(columnDefinition = "date")
    private Date post_date;
}
