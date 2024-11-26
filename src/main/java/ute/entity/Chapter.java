package ute.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @ToString.Exclude
    @JsonManagedReference
    private Book book;

    @OneToMany(mappedBy = "chapter",cascade = CascadeType.ALL)
    private List<ChapterContent> chapterContents;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
