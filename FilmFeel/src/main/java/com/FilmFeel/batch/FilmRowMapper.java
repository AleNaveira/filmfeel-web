package com.FilmFeel.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<FilmDTO> {
    @Override
    public FilmDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilmDTO(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getObject("year", Integer.class),
                rs.getObject("duration", Integer.class),
                rs.getString("synopsis"),
                rs.getString("poster_route")
        );
    }
}
