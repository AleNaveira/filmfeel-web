package com.FilmFeel.controller;


import com.FilmFeel.model.Person;
import com.FilmFeel.model.TypePersonEnum;
import com.FilmFeel.repository.PersonRepository;
import com.FilmFeel.service.PersonServiceImpl;


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


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonServiceImpl personServiceImpl;


    @GetMapping("")
    ModelAndView personIndex() {


        List<Person> persons = personRepository.findAll();

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


        return new ModelAndView("new-person")
                .addObject("person", new Person())
                .addObject("typePersonEnum", TypePersonEnum.values());

    }

    @PostMapping("/nueva-persona")
    ModelAndView submitPerson(@Validated @ModelAttribute("person") Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {


            return new ModelAndView("new-person")
                    .addObject("person", person)
                    .addObject("typePersonEnum", TypePersonEnum.values());
        }

        personRepository.save(person);

        return new ModelAndView("redirect:/personas");
    }

    @PostMapping("/{id}")
    String deletePerson(@PathVariable Long id) {

        personRepository.deleteById(id);

        return "redirect:/personas";


    }
}
