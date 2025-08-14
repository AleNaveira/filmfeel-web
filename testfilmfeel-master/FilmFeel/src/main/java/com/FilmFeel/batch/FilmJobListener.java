package com.FilmFeel.batch;



import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmJobListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(FilmJobListener.class);
    private final JdbcTemplate jdbc;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job comienza...");
        Integer toMigrate = jdbc.queryForObject(
                "SELECT COUNT(*) FROM film WHERE migrate = FALSE", Integer.class);
        log.info(" Pendientes de migrar: {}", toMigrate);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job finalizado con estado: " + jobExecution.getStatus());
        Integer remaining = jdbc.queryForObject(
                "SELECT COUNT(*) FROM film WHERE migrate = FALSE", Integer.class);
        log.info("Quedan sin migrar: {}", remaining);
    }
}
