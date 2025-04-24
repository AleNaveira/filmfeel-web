package com.FilmFeel.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Long id;
    @Column(name = "film_title")
    private String title;
    @Column(name = "film_year")
    private Integer year;
    @Column(name = "film_duration")
    private Integer duration;
    @Column(name = "film_synopsis")
    private String synopsis;
    @Column(name = "film_posterRoute")
    private String posterRoute;
    @Column(name = "migrate")
    private boolean migrate;

    @Column(name = "date_migrate")
    private LocalDate dateMigrate;

    @Transient
    private MultipartFile portada;

    @ManyToOne
    @JoinColumn(name = "fotographer_id")
    private Person photographer;

    @ManyToMany
    @JoinTable(name = "scriptwriters",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "scriptwriter_id"))
    private List<Person> scriptwriters;

    @ManyToMany
    @JoinTable(name = "filmsMusicians",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "musician_id"))
    private List<Person> filmsMusicians;


    @ManyToOne
    @JoinColumn(name = "director_id")
    private Person directors;

    @ManyToMany
    @JoinTable(name = "film_actors",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Person> actors;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "film")
    private List<Review> reviews;

    @OneToMany(mappedBy = "film")
    private List<Score> scores;


}


