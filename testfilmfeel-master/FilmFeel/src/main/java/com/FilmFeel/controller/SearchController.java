package com.FilmFeel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private FilmRepository filmRepository;

    @GetMapping("/buscar")
    public ModelAndView searchFilms(@RequestParam("query") String query) {
        logger.info("Buscando películas con el término:{}", query);
        List<Film> films = filmRepository.findByTitleContainingIgnoreCase(query);
        logger.info("Se encontraron {} películas que coinciden con la búsqueda", films.size());

        ModelAndView modelAndView = new ModelAndView("search-results");
        modelAndView.addObject("films", films);
        modelAndView.addObject("query", query);
        return modelAndView;

    }

}
