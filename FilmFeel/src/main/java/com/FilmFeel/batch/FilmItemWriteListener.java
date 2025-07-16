package com.FilmFeel.batch;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
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
    private final NamedParameterJdbcTemplate namedJdbc;

    @Override
    public void beforeWrite(List<? extends FilmDTO> items) {
        // no-op
    }

    @Override
    public void afterWrite(List<? extends FilmDTO> items) {
        List<Long> ids = items.stream()
                .map(FilmDTO::id)
                .collect(Collectors.toList());

        String sql = """
            UPDATE film
               SET migrate      = TRUE,
                   date_migrate = :date
             WHERE id IN (:ids)
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", LocalDate.now())
                .addValue("ids", ids);

        int updated = namedJdbc.update(sql, params);
        log.info(">>> Películas marcadas como migradas: {}", updated);
    }

    @Override
    public void onWriteError(Exception exception, List<? extends FilmDTO> items) {
        log.error("Error al escribir chunk de películas", exception);
    }
}

