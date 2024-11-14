package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;
    @Column(columnDefinition = "date")
    private LocalDateTime post_date;

    @ManyToOne
    @JoinColumn(name = "fk_chapter")
    private Chapter chapter;

    @ManyToOne
    @JoinColumn(name="fk_account")
    private Account account;

}
