package com.FilmFeel.batch;


import com.FilmFeel.model.Film;
import com.FilmFeel.repository.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
public class FilmMigrationJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private Job filmMigrationJob;

    @BeforeEach
    void setUp() {
        // Asociar el Job concreto a los utils
        jobLauncherTestUtils.setJob(filmMigrationJob);

        // Prepara la BD: una película no migrada
        filmRepository.deleteAll();
        Film f = new Film();
        f.setTitle("Prueba");
        f.setYear(2025);
        f.setDuration(120);
        f.setSynopsis("Sinopsis de prueba");
        filmRepository.save(f);
    }

    @Test
    void testJobCompletesSuccessfully() throws Exception {
        JobExecution execution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder()
                        .addString("output.file.name", "test_films.csv")
                        .addDate("runDate", new Date())
                        .toJobParameters()
        );

        // Verifica que el job terminó OK
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        // Opcional: que cada Step haya completado bien
        execution.getStepExecutions()
                .forEach(step ->
                        assertThat(step.getExitStatus().getExitCode())
                                .isEqualTo("COMPLETED")
                );
    }
}
