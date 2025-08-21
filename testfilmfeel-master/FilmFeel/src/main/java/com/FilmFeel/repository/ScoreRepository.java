package com.FilmFeel.repository;

import com.FilmFeel.model.Film;
import com.FilmFeel.model.Score;
import com.FilmFeel.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByFilmId(Long filmId);
    Optional <Score>findByUserEntityAndFilm(UserEntity userEntity, Film film);
}
