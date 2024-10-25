package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "nvarchar(50)")
    private String name;
    @Column(columnDefinition = "nvarchar(50)")
    private String author;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String description;
    private List<Genre> genres;
    @Column(columnDefinition = "nvarchar(20)")
    private String type;
    @Column(columnDefinition = "nvarchar(50)")
    private String thumbnail;
    @Column
    private Integer price;
    @Column(columnDefinition = "nvarchar(100)")
    private String keysearch;
    @Column
    private Boolean is_delete;
    private List<Chapter> chapters;
}
