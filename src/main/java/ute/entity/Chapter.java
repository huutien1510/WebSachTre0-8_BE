package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "nvarchar(100)")
    private String title;
    @Column(columnDefinition = "date")
    private Date pushlishDate;
    @Column
    private Integer viewCount;
    @Column
    private Integer chapterNumber;

    @ManyToMany(mappedBy = "chapters")
    private List<Account> accounts;

    @ManyToOne
    @JoinColumn(name = "fk_book")
    private Book book;

    @OneToMany(mappedBy = "chapter",cascade = CascadeType.ALL)
    private List<ChapterContent> chapterContents;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
