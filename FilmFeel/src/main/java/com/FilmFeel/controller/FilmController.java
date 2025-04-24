package com.FilmFeel.controller;

import com.FilmFeel.model.*;
import com.FilmFeel.repository.FilmRepository;

import com.FilmFeel.repository.ReviewRepository;
import com.FilmFeel.repository.ScoreRepository;
import com.FilmFeel.service.PersonServiceImpl;
import com.FilmFeel.service.StorageServiceImpl;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/peliculas")
public class FilmController {


    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private PersonServiceImpl personServiceImpl;

    @Autowired
    private StorageServiceImpl storageService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ScoreRepository scoreRepository;


    @GetMapping("")
    ModelAndView index(@PageableDefault(sort = "title", size = 5) Pageable pageable) {


        Page<Film> films = filmRepository.findAll(pageable);


        ModelAndView modelAndView = new ModelAndView("films");
        modelAndView.addObject("films", films);
        modelAndView.addObject("currentPage", films.getNumber());
        modelAndView.addObject("totalPages", films.getTotalPages());
        modelAndView.addObject("pageSize", films.getSize());


        Map<Long, List<Person>> filmActorsMap = new HashMap<>();
        Map<Long, Person> filmDirectorsMap = new HashMap<>();
        Map<Long, List<Person>> filmScriptwritersMap = new HashMap<>();
        Map<Long, Person> filmPhotographersMap = new HashMap<>();
        Map<Long, List<Person>> filmMusiciansMap = new HashMap<>();


        for (Film film : films.getContent()) {
            Long filmId = film.getId();


            filmActorsMap.put(filmId, film.getActors());
            filmDirectorsMap.put(filmId, film.getDirectors());
            filmScriptwritersMap.put(filmId, film.getScriptwriters());
            filmPhotographersMap.put(filmId, film.getPhotographer());
            filmMusiciansMap.put(filmId, film.getFilmsMusicians());


        }


        modelAndView.addObject("filmActorsMap", filmActorsMap);
        modelAndView.addObject("filmDirectorsMap", filmDirectorsMap);
        modelAndView.addObject("filmScriptwritersMap", filmScriptwritersMap);
        modelAndView.addObject("filmPhotographersMap", filmPhotographersMap);
        modelAndView.addObject("filmMusiciansMap", filmMusiciansMap);

        return modelAndView;
    }


    @GetMapping("/nueva-pelicula")
    ModelAndView newFilm() {


        List<Person> photographers = personServiceImpl.getAllPhotographers();
        List<Person> scriptwriters = personServiceImpl.getAllScriptwriters();
        List<Person> directors = personServiceImpl.getAllDirectors();
        List<Person> musicians = personServiceImpl.getAllMusicians();
        List<Person> actors = personServiceImpl.getAllActors();


        return new ModelAndView("new-film")
                .addObject("film", new Film())
                .addObject("actors", actors)
                .addObject("directors", directors)
                .addObject("musicians", musicians)
                .addObject("scriptwriters", scriptwriters)
                .addObject("photographers", photographers);


    }

    @PostMapping("/nueva-pelicula")
    ModelAndView createFilm(@Validated Film film, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || film.getPortada().isEmpty()) {

            if (film.getPortada().isEmpty()) {
                bindingResult.rejectValue("portada", "MultipartNotEmpty");


            }


            return new ModelAndView("new-film")
                    .addObject("film", film)
                    .addObject("photographers", personServiceImpl.getAllPhotographers());


        }

        String posterRoute = storageService.storage(film.getPortada());
        film.setPosterRoute(posterRoute);


        filmRepository.save(film);

        return new ModelAndView("redirect:/peliculas");

    }

    @GetMapping("/{id}")
    ModelAndView filmDetails(@PathVariable Long id) {


        ModelAndView model = new ModelAndView("film_detail");
        Film film = filmRepository.findById(id).orElse(null);


        if (film == null) {

            return new ModelAndView("redirect:/peliculas");


        }
        List<Review> reviews = reviewRepository.findByFilmIdWithUser(id);

        System.out.println("Reviews size: " + reviews.size());

        Person photographer = film.getPhotographer();
        Person director = film.getDirectors();
        List<Person> scriptwriters = film.getScriptwriters();
        List<Person> musicians = film.getFilmsMusicians();
        List<Person> actors = film.getActors();


        List<Score> scores = scoreRepository.findByFilmId(id);
        double averageScore = 0.0;
        if (!scores.isEmpty()) {
            averageScore = scores.stream()
                    .mapToInt(Score::getValue)
                    .average()
                    .orElse(0.0);

        }


        model.addObject("film", film)
                .addObject("photographer", photographer)
                .addObject("actors", actors)
                .addObject("director", director)
                .addObject("scriptwriters", scriptwriters)
                .addObject("musicians", musicians);

        model.addObject("reviews", reviews);
        model.addObject("review", new Review());


        model.addObject("averageScore", averageScore);
        return model;
    }

    @GetMapping("/{id}/editar")
    ModelAndView editFilm(@PathVariable Long id) {


        Film film = filmRepository.getReferenceById(id);


        return new ModelAndView("/edit-film")
                .addObject("film", film);

    }


    @PostMapping("/{id}/editar")
    ModelAndView updateFilm(@PathVariable Long id, @Validated Film film
            , BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {


            return new ModelAndView("editar-pelicula")
                    .addObject("film", film);
        }

        Film peliculaFromDb = filmRepository.getReferenceById(id);
        peliculaFromDb.setTitle(film.getTitle());
        peliculaFromDb.setYear(film.getYear());
        peliculaFromDb.setDuration(film.getDuration());
        peliculaFromDb.setSynopsis(film.getSynopsis());


        if (!film.getPortada().isEmpty()) {
            storageService.delete(peliculaFromDb.getPosterRoute());
            String posterRoute = storageService.storage(film.getPortada());
            peliculaFromDb.setPosterRoute(posterRoute);


        }

        filmRepository.save(peliculaFromDb);


        return new ModelAndView("redirect:/peliculas");


    }


    @PostMapping("/{id}/eliminar")

    @Transactional
    String deleteFilm(@PathVariable Long id) {


        Film film = filmRepository.getReferenceById(id);

        reviewRepository.deleteByFilm(film);
        filmRepository.delete(film);
        storageService.delete(film.getPosterRoute());


        return "redirect:/peliculas";


    }


}




