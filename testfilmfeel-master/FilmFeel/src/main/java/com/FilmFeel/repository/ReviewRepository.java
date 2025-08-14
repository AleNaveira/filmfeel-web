package com.FilmFeel.repository;

import com.FilmFeel.model.Film;
import com.FilmFeel.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.userEntity WHERE r.film.id = :filmId")
    List<Review> findByFilmIdWithUser(@Param("filmId") Long filmId);


    List<Review> findByFilmId(Long filmId);

    Optional<Review> findByUserEntityIdAndFilmId(Long userId, Long filmId);

    void deleteByFilm(Film film);


}
