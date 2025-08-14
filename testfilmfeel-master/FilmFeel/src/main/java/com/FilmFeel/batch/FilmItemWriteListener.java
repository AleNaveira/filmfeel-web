package com.FilmFeel.batch;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmItemWriteListener implements ItemWriteListener<FilmDTO> {

    private static final Logger log = LoggerFactory.getLogger(FilmItemWriteListener.class);
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public void beforeWrite(Chunk<? extends FilmDTO> chunk) {
        log.debug("A punto de escribir chunk de {} películas", chunk.getItems().size());
    }

    @Override
    public void afterWrite(Chunk<? extends FilmDTO> chunk) {
        List<Long> ids = chunk.getItems().stream()
                .map(FilmDTO::id)
                .collect(Collectors.toList());

        String sql = """
            UPDATE film
               SET migrate      = TRUE,
                   date_migrate = :today
             WHERE film_id IN (:ids)
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("today", LocalDate.now())
                .addValue("ids", ids);

        int updated = jdbc.update(sql, params);
        log.info("Marcadas {} películas como migradas", updated);
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends FilmDTO> chunk) {
        log.error(" Error escribiendo chunk", exception);
    }
}
