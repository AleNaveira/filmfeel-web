package com.FilmFeel.batch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FilmJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job exportFilmsJob;

    // Se ejecuta 5 min (300_000 ms) después de arrancar, y cada 5 min
    @Scheduled(fixedRate = 300_000, initialDelay = 0)
    @SneakyThrows
    public void runEveryFiveMinutes() {
        jobLauncher.run(exportFilmsJob,
                new JobParametersBuilder()
                        .addString("outputFile", "films-" + LocalDate.now() + ".csv")
                        .addLong("timestamp", System.currentTimeMillis())
                        .toJobParameters()
        );
    }
}

