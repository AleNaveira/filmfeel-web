package com.FilmFeel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.FilmFeel.model.CustomUserDetails;
import com.FilmFeel.model.Film;
import com.FilmFeel.model.Score;
import com.FilmFeel.repository.FilmRepository;
import com.FilmFeel.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

@Controller
@RequestMapping("/scores")
public class ScoreController {
    private static final Logger logger = LoggerFactory.getLogger(ScoreController.class);


    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private FilmRepository filmRepository;

    @PostMapping("/submit/{filmId}")
    public ModelAndView submitScore(@PathVariable Long filmId, @RequestParam Integer scoreValue, @AuthenticationPrincipal CustomUserDetails currentUser) {
        logger.info("Intentando enviar puntuación para la película con ID:{}", filmId);
        Film film = filmRepository.findById(filmId).orElse(null);
        if (film != null && scoreValue >= 1 && scoreValue <= 5) {
            Score score = new Score();
            score.setFilm(film);
            score.setValue(scoreValue);

            if (currentUser != null) {
                score.setUserEntity(currentUser.getUser());
            }
            scoreRepository.save(score);
            logger.info("Puntuación guardada correctamente para la película con ID:{}", filmId);

        }else{
            logger.warn("No se pudo guardar la puntuación");
        }
        return new ModelAndView("redirect:/peliculas/" + filmId);
    }

    @GetMapping("/average/{filmId}")
    public ModelAndView calculateAverageScore(@PathVariable Long filmId) {
        logger.info("Calculando la puntuación promedio para la película con ID:{}", filmId);
        List<Score> scores = scoreRepository.findAll();
        if (scores == null) {
            scores = Collections.emptyList();

        } else {

        }
        OptionalDouble average = scores.stream().mapToInt(Score::getValue).average();
        double averageScore = average.isPresent() ? average.getAsDouble() : 0;

           logger.info("La puntuación promedio calculada para la película con ID: {} es : {}",filmId,averageScore);
        return new ModelAndView("film_detail")
                .addObject("averageScore", averageScore)
                .addObject("film", filmRepository.findById(filmId).orElse(null));
    }


    @PostMapping("/{id}")
    String deleteScore(@PathVariable Long id) {
        logger.info("Eliminando puntuación con ID:{}", id);
        scoreRepository.deleteById(id);
        logger.info("Puntuación eliminada exitosamente con ID: {}", id);
        return "redirect:/";
    }
}
