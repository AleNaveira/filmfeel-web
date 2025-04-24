package com.FilmFeel.controller;

import com.FilmFeel.model.Film;
import com.FilmFeel.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private FilmRepository filmRepository;

    @GetMapping("/buscar")
    public ModelAndView searchFilms(@RequestParam("query") String query) {
        List<Film> films = filmRepository.findByTitleContainingIgnoreCase(query);

        ModelAndView modelAndView = new ModelAndView("search-results");
        modelAndView.addObject("films", films);
        modelAndView.addObject("query", query);
        return modelAndView;

    }

}
