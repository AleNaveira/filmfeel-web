package com.FilmFeel.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;

    @Qualifier("exportFilmsJob")
    private final Job filmMigrationJob;

    @Value("${batch.output.dir}")
    private String outputDir;

    @Scheduled(cron = "0 */5 * * * *")
    public void launchJob() throws Exception {
        System.out.println("Intentando lanzar el batch...");
        String fileName = outputDir + "/" + LocalDate.now() + "_films.csv";


        var params = new JobParametersBuilder()
                .addString("outputFile", fileName)
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        System.out.println("Params generados: " + params);
        jobLauncher.run(filmMigrationJob, params);
    }
}
