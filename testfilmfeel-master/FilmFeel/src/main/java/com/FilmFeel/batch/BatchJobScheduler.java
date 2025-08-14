package com.FilmFeel.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job filmMigrationJob;

    @Value("${batch.output.dir}")
    private String outputDir;

    private final JdbcTemplate jdbc;

    public BatchJobScheduler(JobLauncher jobLauncher,
                             @Qualifier("exportFilmsJob") Job filmMigrationJob,
    JdbcTemplate jdbc) {
        this.jobLauncher = jobLauncher;
        this.filmMigrationJob = filmMigrationJob;
        this.jdbc= jdbc;

    }



    @Scheduled(cron = "0 */5 * * * *", zone = "Europe/Madrid")
    public void launchJob() throws Exception {

        Integer pending = jdbc.queryForObject(
                "SELECT COUNT(*) FROM film WHERE migrate = FALSE", Integer.class);
        System.out.println("🔎 Pendientes justo antes del job (APP): " + pending);

        String fileName = outputDir + "/" + LocalDate.now() + "_films.csv";


        var params = new JobParametersBuilder()
                .addString("outputFile", fileName)
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();


try {
    var execution = jobLauncher.run(filmMigrationJob, params);


    if (!execution.getAllFailureExceptions().isEmpty()) {
        System.out.println(" Excepciones del job:");
        execution.getAllFailureExceptions().forEach(Throwable::printStackTrace);
    }
}catch (Exception e){
    System.out.println("Falló el run del job:");
    e.printStackTrace();
}
    }
}
