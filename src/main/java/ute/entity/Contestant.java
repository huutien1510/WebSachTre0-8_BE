package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contestant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Contest contest;
    @ManyToOne
    @JoinColumn(name = "fk_account")
    private Account account;
    @Column(columnDefinition = "nvarchar(100)")
    private String submission;
    @Column(columnDefinition = "date")
    private LocalDateTime submit_time;
}
