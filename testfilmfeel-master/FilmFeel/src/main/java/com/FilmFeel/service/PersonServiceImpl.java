package com.FilmFeel.service;

import com.FilmFeel.model.Person;
import com.FilmFeel.model.TypePersonEnum;
import com.FilmFeel.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;


    @Override
    public List<Person> listAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person getPerson(Long id) {
        return personRepository.getReferenceById(id);
    }

    @Override
    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    public List<Person> findByTypePerson(TypePersonEnum typePersonEnum) {
        return personRepository.findByTypePerson(typePersonEnum);
    }


    @Override
    public List<Person> getAllPhotographers() {
        return personRepository.findByTypePerson(TypePersonEnum.FOTOGRAFO);
    }

    @Override
    public List<Person> getAllScriptwriters() {
        return personRepository.findByTypePerson(TypePersonEnum.GUIONISTA);
    }

    @Override
    public List<Person> getAllMusicians() {

        return personRepository.findByTypePerson(TypePersonEnum.MUSICO);
    }

    @Override
    public List<Person> getAllDirectors() {
        return personRepository.findByTypePerson(TypePersonEnum.DIRECTOR);
    }

    @Override
    public List<Person> getAllActors() {
        return personRepository.findByTypePerson(TypePersonEnum.ACTOR);
    }


}
