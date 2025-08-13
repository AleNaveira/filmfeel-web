package com.FilmFeel.batch;

import com.FilmFeel.dto.FilmDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmWriterListener implements ItemWriteListener<FilmDTO> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void beforeWrite(Chunk<? extends FilmDTO> items) {
        log.debug("A punto de escribir {} películas", items.getItems().size());
    }

    @Override
    public void afterWrite(Chunk<? extends FilmDTO> items) {
        // obtenemos la lista real con .getItems()
        List<Long> ids = items.getItems().stream()
                .map(FilmDTO::getId)
                .collect(Collectors.toList());

        String sql = "UPDATE film"
                + "SET migrate = TRUE, date_migrate = :date "
                + "WHERE film_id IN (:ids)";
        var params = new MapSqlParameterSource()
                .addValue("date", LocalDate.now())
                .addValue("ids", ids);

        int updated = new NamedParameterJdbcTemplate(jdbcTemplate)
                .update(sql, params);

        log.info("Fecha de migración marcada para {} películas", updated);
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends FilmDTO> items) {
        log.error("Error al escribir películas", exception);
    }
}