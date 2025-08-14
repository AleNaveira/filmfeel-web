package com.FilmFeel.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<FilmDTO> {
    @Override
    public FilmDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

        FilmDTO film = new FilmDTO(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getInt("year"),
                rs.getInt("duration"),
                rs.getString("synopsis"),
                rs.getString("posterRoute")
        );
        return film;
    }

}

