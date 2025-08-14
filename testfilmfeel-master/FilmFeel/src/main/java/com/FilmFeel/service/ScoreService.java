package com.FilmFeel.service;

import com.FilmFeel.model.Score;
import com.FilmFeel.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    public Score saveScore(Score score) {
        return scoreRepository.save(score);
    }

    public double calculateAverageScore() {
        List<Score> scores = scoreRepository.findAll();
        return scores.stream().mapToInt(Score::getValue).average().orElse(0.0);
    }
}
