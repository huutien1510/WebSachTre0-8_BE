package ute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CheckinHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_account")
    private Account account;

    @Column
    private Integer bonusPoint;
    @Column(columnDefinition = "date")
    private Date checkinDate;
}
