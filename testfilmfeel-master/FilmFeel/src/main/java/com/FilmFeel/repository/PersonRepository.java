package com.FilmFeel.repository;

import com.FilmFeel.model.Person;
import com.FilmFeel.model.TypePersonEnum;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByTypePerson(TypePersonEnum typePerson);


}


