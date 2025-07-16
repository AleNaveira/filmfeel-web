package com.FilmFeel.batch;



import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FilmJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job exportFilmsJob;

    /** Se lanza cada día a las 02:00 AM */
    @Scheduled(cron = "0 0 2 * * *")
    @SneakyThrows
    public void runDailyExport() {
        String fileName = "films-" + LocalDate.now() + ".csv";
        var params = new JobParametersBuilder()
                .addString("outputFile", fileName)
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(exportFilmsJob, params);
    }
}

