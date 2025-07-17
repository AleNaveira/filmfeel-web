package com.FilmFeel.batch;


public record FilmDTO(
        Long id,
        String title,
        Integer year,
        Integer duration,
        String synopsis,
        String posterRoute
) { }

