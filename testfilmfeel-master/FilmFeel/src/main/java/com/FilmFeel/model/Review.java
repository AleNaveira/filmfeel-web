package com.FilmFeel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="review", uniqueConstraints = { @UniqueConstraint (columnNames = {"user_id", "film_id"})
        }
)

public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    @Column(name = "review_title")
    private String reviewTitle;
    @Column(name = "review_text", columnDefinition = " LONGTEXT")
    private String reviewText;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "review_date")
    private LocalDate reviewDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;
}


