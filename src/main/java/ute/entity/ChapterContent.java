package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChapterContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "nvarchar(100)")
    private String link;
    @Column
    private Integer content_number;

    @ManyToOne
    @JoinColumn(name = "fk_chapter")
    private Chapter chapter;
}
