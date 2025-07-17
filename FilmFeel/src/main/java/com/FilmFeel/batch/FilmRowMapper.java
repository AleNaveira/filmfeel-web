package com.FilmFeel.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<FilmDTO> {
    @Override
    public FilmDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilmDTO(
                rs.getLong("film_id"),
                rs.getString("film_title"),
                rs.getObject("film_year", Integer.class),
                rs.getObject("film_duration", Integer.class),
                rs.getString("film_synopsis"),
                rs.getString("poster_route")
        );
    }
}

