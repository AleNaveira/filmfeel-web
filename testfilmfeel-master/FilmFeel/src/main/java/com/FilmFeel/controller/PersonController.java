package com.FilmFeel.controller;


import com.FilmFeel.model.Person;
import com.FilmFeel.model.TypePersonEnum;
import com.FilmFeel.repository.PersonRepository;
import com.FilmFeel.service.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/personas")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonServiceImpl personServiceImpl;


    @GetMapping("")
    ModelAndView personIndex() {

        logger.info("Accediendo a la lista de personas");


        List<Person> persons = personRepository.findAll();
        logger.info("Se encontraron {} personas en total", persons.size());

        List<Person> actors = personServiceImpl.findByTypePerson(TypePersonEnum.ACTOR);

        List<Person> photographers = personServiceImpl.findByTypePerson(TypePersonEnum.FOTOGRAFO);
        List<Person> directors = personServiceImpl.findByTypePerson(TypePersonEnum.DIRECTOR);
        List<Person> scriptwriters = personServiceImpl.findByTypePerson(TypePersonEnum.GUIONISTA);
        List<Person> musicians = personServiceImpl.findByTypePerson(TypePersonEnum.MUSICO);

        return new ModelAndView("persons_list")
                .addObject("persons", persons)
                .addObject("actors", actors)
                .addObject("photographers", photographers)
                .addObject("directors", directors)
                .addObject("scriptwriters", scriptwriters)
                .addObject("musicians", musicians);


    }


    @GetMapping("/nueva-persona")
    ModelAndView newPerson() {

        logger.info("Accediendo al formulario de creación de nueva persona");


        return new ModelAndView("new-person")
                .addObject("person", new Person())
                .addObject("typePersonEnum", TypePersonEnum.values());

    }

    @PostMapping("/nueva-persona")
    ModelAndView submitPerson(@Validated @ModelAttribute("person") Person person, BindingResult bindingResult) {
        logger.info("Intentando crear nueva persona : {}", person.getName());
        if (bindingResult.hasErrors()) {
            logger.warn("Errores de validación al crear la persona: {}",person.getName());


            return new ModelAndView("new-person")
                    .addObject("person", person)
                    .addObject("typePersonEnum", TypePersonEnum.values());
        }

        personRepository.save(person);
        logger.info("Persona creada con éxito");

        return new ModelAndView("redirect:/personas");
    }

    @PostMapping("/{id}")
    String deletePerson(@PathVariable Long id) {

        logger.info("Eliminando persona con ID:{}", id);

        personRepository.deleteById(id);
        logger.info("Persona eliminada exitosamente con ID: {}", id);

        return "redirect:/personas";


    }
}
