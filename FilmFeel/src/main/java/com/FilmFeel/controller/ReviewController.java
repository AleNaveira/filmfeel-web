package com.FilmFeel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.FilmFeel.model.CustomUserDetails;
import com.FilmFeel.model.Film;
import com.FilmFeel.model.Review;
import com.FilmFeel.model.UserEntity;
import com.FilmFeel.repository.FilmRepository;
import com.FilmFeel.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reviews")

public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FilmRepository filmRepository;

    @PostMapping("/submit/{filmId}")
    public ModelAndView submitReview(@PathVariable Long filmId, @ModelAttribute Review review, BindingResult bindingResult, @ModelAttribute("user") CustomUserDetails currentUser) {
        logger.info("Intentando enviar una critica para la película con ID:{}", filmId);
        Film film = filmRepository.findById(filmId).orElse(null);
        if (film != null && currentUser != null) {
            review.setFilm(film);
            review.setUserEntity(currentUser.getUser());
            review.setReviewDate(LocalDate.now());
            reviewRepository.save(review);
            logger.info("Crítica guardada correctamente para la película con ID: {}", filmId);
        }else{
            logger.warn("No se pudo enviar la critica.");
        }
        return new ModelAndView("redirect:/peliculas/" + filmId);
    }

    @GetMapping("/lista/{filmId}")
    public ModelAndView listReviews(@PathVariable Long filmId) {
        logger.info("Accediendo a la lista de reviews para la pelicula con ID:{}", filmId);
        List<Review> reviews = reviewRepository.findByFilmIdWithUser(filmId);
        logger.info("Se encontraron {} críticas para la película con ID: {}", reviews.size(),filmId);
        return new ModelAndView("film_detail")
                .addObject("reviews", reviews);
    }

    @PostMapping("/{id}")
    String deleteReview(@PathVariable Long id) {
        logger.info("Eliminando crítica con ID: {}", id);
        reviewRepository.deleteById(id);
        logger.info("Critica eliminada exitosamente con ID: {}", id);

        return "redirect:/";


    }

}
