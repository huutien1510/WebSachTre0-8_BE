package ute.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "nvarchar(100)")
    private String title;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;
    @Column(columnDefinition = "nvarchar(100)")
    private String author;
    @Column(columnDefinition = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    @Column(columnDefinition = "nvarchar(255)")
    private String image;

}
