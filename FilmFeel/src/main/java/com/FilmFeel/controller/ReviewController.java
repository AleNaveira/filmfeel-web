package com.FilmFeel.controller;


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

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FilmRepository filmRepository;

    @PostMapping("/submit/{filmId}")
    public ModelAndView submitReview(@PathVariable Long filmId, @ModelAttribute Review review, BindingResult bindingResult, @ModelAttribute("user") CustomUserDetails currentUser) {
        Film film = filmRepository.findById(filmId).orElse(null);
        if (film != null && currentUser != null) {
            review.setFilm(film);
            review.setUserEntity(currentUser.getUser());
            review.setReviewDate(LocalDate.now());
            reviewRepository.save(review);
        }
        return new ModelAndView("redirect:/peliculas/" + filmId);
    }

    @GetMapping("/lista/{filmId}")
    public ModelAndView listReviews(@PathVariable Long filmId) {
        List<Review> reviews = reviewRepository.findByFilmIdWithUser(filmId);
        return new ModelAndView("film_detail")
                .addObject("reviews", reviews);
    }

    @PostMapping("/{id}")
    String deleteReview(@PathVariable Long id) {
        reviewRepository.deleteById(id);

        return "redirect:/";


    }

}
