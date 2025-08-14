package com.FilmFeel.repository;

import com.FilmFeel.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByFilmId(Long filmId);
}
