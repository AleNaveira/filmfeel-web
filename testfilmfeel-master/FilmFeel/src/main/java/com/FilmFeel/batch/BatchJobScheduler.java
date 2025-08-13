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


    @Scheduled(cron = "0 */5 * * * *")
    public void launchJob() throws Exception {
        System.out.println("Intentando lanzar el batch...");
        // 👇 DATO CLAVE: lo que la APP ve AHORA MISMO
        Integer pending = jdbc.queryForObject(
                "SELECT COUNT(*) FROM film WHERE migrate = FALSE", Integer.class);
        System.out.println("🔎 Pendientes justo antes del job (APP): " + pending);

        String fileName = outputDir + "/" + LocalDate.now() + "_films.csv";


        var params = new JobParametersBuilder()
                .addString("outputFile", fileName)
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        System.out.println("Params generados: " + params);


        var execution = jobLauncher.run(filmMigrationJob, params);
        // 🔍 Depuración: mostrar estado
        System.out.println("Estado inicial del job: " + execution.getStatus());
        execution.getStepExecutions().forEach(stepExec ->
                System.out.println("Step: " + stepExec.getStepName() +
                        " | ReadCount=" + stepExec.getReadCount() +
                        " | WriteCount=" + stepExec.getWriteCount())
        );

        System.out.println("Estado del job: " + execution.getStatus());
        execution.getStepExecutions().forEach(se -> System.out.println(
                "Step " + se.getStepName() +
                        " read=" + se.getReadCount() +
                        " write=" + se.getWriteCount() +
                        " status=" + se.getStatus()
        ));

        // 👇 imprime cualquier excepción del job o del step (mapper, writer, etc.)
        if (!execution.getAllFailureExceptions().isEmpty()) {
            System.out.println("❌ Excepciones del job:");
            execution.getAllFailureExceptions().forEach(Throwable::printStackTrace);
        }
    }
}
