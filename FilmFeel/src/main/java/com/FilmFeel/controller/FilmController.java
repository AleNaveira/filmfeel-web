package com.FilmFeel.controller;

import com.FilmFeel.model.*;
import com.FilmFeel.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);


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
        logger.info("Accediendo al listado de películas");

        Page<Film> films = filmRepository.findAll(pageable);
        logger.info("Se encontraron {} películas", films.getTotalElements());


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

        logger.info("Accediendo al formulario de nueva película");


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
        logger.info("Intentando crear nueva película : {}", film.getTitle());
        if (bindingResult.hasErrors() || film.getPortada().isEmpty()) {

            if (film.getPortada().isEmpty()) {
                logger.warn("El campo portada está vacío");
                bindingResult.rejectValue("portada", "MultipartNotEmpty");
            }
            logger.warn("Errores de validación al crear la película: {}", film.getTitle());


            return new ModelAndView("new-film")
                    .addObject("film", film)
                    .addObject("photographers", personServiceImpl.getAllPhotographers());


        }

        String posterRoute = storageService.storage(film.getPortada());
        film.setPosterRoute(posterRoute);


        filmRepository.save(film);
        logger.info("Película creada exitosamente");

        return new ModelAndView("redirect:/peliculas");

    }

    @GetMapping("/{id}")
    ModelAndView filmDetails(@PathVariable Long id) {

        logger.info("Accediendo a los detalles de la película con ID: {}", id);


        ModelAndView model = new ModelAndView("film_detail");
        Film film = filmRepository.findById(id).orElse(null);


        if (film == null) {

            logger.warn("No se encontró la película con ID: {}", id);

            return new ModelAndView("redirect:/peliculas");


        }
        List<Review> reviews = reviewRepository.findByFilmIdWithUser(id);
        logger.info("La película tiene {} reviews", reviews.size());
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
        logger.info("Accediendo al formulario de edición de la película con ID: {}", id);


        Film film = filmRepository.getReferenceById(id);


        return new ModelAndView("/edit-film")
                .addObject("film", film)
                .addObject("actors", personServiceImpl.findByTypePerson(TypePersonEnum.ACTOR))
                .addObject("filmMusicians", personServiceImpl.findByTypePerson(TypePersonEnum.MUSICO))
                .addObject("scriptwriters", personServiceImpl.findByTypePerson(TypePersonEnum.GUIONISTA))
                .addObject("directors", personServiceImpl.findByTypePerson(TypePersonEnum.DIRECTOR))
                .addObject("photographer", personServiceImpl.findByTypePerson(TypePersonEnum.FOTOGRAFO))
                .addObject("selectedActors", film.getActors())
                .addObject("selectedMusicians", film.getFilmsMusicians())
                .addObject("selectedScriptwriters", film.getScriptwriters())
                .addObject("selectedDirectors", film.getDirectors())
                .addObject("selectedPhotographers", film.getPhotographer());


    }


    @PostMapping("/{id}/editar")
    ModelAndView updateFilm(@PathVariable Long id, @Validated Film film
            , BindingResult bindingResult) {

        logger.info("Intentando actualizar la película con ID: {}", id);


        if (bindingResult.hasErrors()) {

            logger.warn("Errores de validación al actualizar la película con ID: {}", id);


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
            logger.info("Portada actualizada para la pelicula con ID: {}", id);


        }

        filmRepository.save(peliculaFromDb);
        logger.info("Película actualizada con éxito");


        return new ModelAndView("redirect:/peliculas");


    }


    @PostMapping("/{id}/eliminar")

    @Transactional
    String deleteFilm(@PathVariable Long id) {
        logger.info("Eliminando la película con ID: {}", id);


        Film film = filmRepository.getReferenceById(id);

        reviewRepository.deleteByFilm(film);
        filmRepository.delete(film);
        storageService.delete(film.getPosterRoute());
        logger.info("Película eliminada con éxito");


        return "redirect:/peliculas";


    }


}




