package ute.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Float star;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;

    @Column(columnDefinition = "date")
    private Date postDate;

    @ManyToOne
    @JoinColumn(name = "fk_book")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "fk_account")
    private Account account;

}
