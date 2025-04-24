package com.FilmFeel.service;


import com.FilmFeel.model.Film;
import com.FilmFeel.model.Review;
import com.FilmFeel.model.UserEntity;
import com.FilmFeel.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


    public Review saveReview(Review review, UserEntity user, Film film) {
        review.setUserEntity(user);
        review.setFilm(film);
        review.setReviewDate(LocalDate.now());
        return reviewRepository.save(review);
    }


    public List<Review> getReviewsByFilmId(Long filmId) {
        return reviewRepository.findByFilmIdWithUser(filmId);
    }


    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findAll().stream()
                .filter(review -> review.getUserEntity() != null && review.getUserEntity().getId().equals(userId))
                .toList();
    }


    public Optional<Review> findByUserAndFilm(Long userId, Long filmId) {
        return reviewRepository.findByUserEntityIdAndFilmId(userId, filmId);
    }


    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }


    public void deleteReviewsByFilm(Film film) {
        reviewRepository.deleteByFilm(film);
    }
}


