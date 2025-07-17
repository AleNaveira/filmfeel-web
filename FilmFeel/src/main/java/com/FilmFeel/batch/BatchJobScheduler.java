package com.FilmFeel.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job filmMigrationJob;

    @Scheduled(cron = "0 */5 * * * *")  // cada día a medianoche
    public void launchJob() throws Exception {
        System.out.println("Intentando lanzar el batch...");
        String fileName = LocalDate.now() + "_films.csv";
        var params = new JobParametersBuilder()
                .addString("outputFile", fileName)
                .addDate("runDate", new Date())
                .toJobParameters();
        jobLauncher.run(filmMigrationJob, params);
    }
}
