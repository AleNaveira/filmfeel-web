package com.FilmFeel.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "person_surname")
    private String surname;

    @Enumerated(EnumType.STRING)
    private TypePersonEnum typePerson;


    @OneToMany(mappedBy = "photographer")
    private List<Film> filmsPhotographers;

    @ManyToMany(mappedBy = "scriptwriters")
    private List<Film> scriptwriters;

    @ManyToMany(mappedBy = "filmsMusicians")
    private List<Film> filmsMusicians;

    @OneToMany(mappedBy = "directors")
    private List<Film> filmsDirector;

    @ManyToMany(mappedBy = "actors")
    private List<Film> filmsActors;

    @Override
    public String toString() {
        return name + " " + surname;
    }

}

