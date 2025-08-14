package com.FilmFeel.service;

import com.FilmFeel.model.Person;
import com.FilmFeel.model.TypePersonEnum;


import java.util.List;

public interface PersonService {

    public List<Person> listAllPersons();

    public Person savePerson(Person person);

    public Person getPerson(Long id);

    public Person updatePerson(Person person);

    public void deletePerson(Long id);

    List<Person> findByTypePerson(TypePersonEnum typePerson);

    public List<Person> getAllPhotographers();

    public List<Person> getAllScriptwriters();

    public List<Person> getAllMusicians();

    public List<Person> getAllDirectors();

    public List<Person> getAllActors();


}
