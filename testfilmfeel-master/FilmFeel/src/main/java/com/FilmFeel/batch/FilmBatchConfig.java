package com.FilmFeel.batch;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class FilmBatchConfig {

    private final DataSource dataSource;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;


    private final FilmJobListener jobListener;
    private final FilmStepListener stepListener;
    private final FilmItemWriteListener writeListener;

    @Bean
    public JdbcPagingItemReader<FilmDTO> filmReader() throws Exception {
        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(dataSource);

        factory.setSelectClause(
                "SELECT film_id AS id, " +
                        "       film_title AS title, " +
                        "       film_year AS \"year\", " +
                        "       film_duration AS duration, " +
                        "       film_synopsis AS synopsis, " +
                        "       film_poster_route AS posterRoute"
        );




        factory.setFromClause("FROM film");
        factory.setWhereClause("WHERE migrate = FALSE");
        factory.setSortKey("film_id");

        var queryProvider = factory.getObject();

        System.out.println("📄 SQL que se ejecutará: " +
                queryProvider.generateFirstPageQuery(50));
        System.out.println("📄 SQL siguiente página: " +
                queryProvider.generateRemainingPagesQuery(50));

        try (var c = dataSource.getConnection()) {
            c.getMetaData().getURL();
        }

        return new JdbcPagingItemReaderBuilder<FilmDTO>()
                .name("filmReader")
                .dataSource(dataSource)
                .queryProvider(factory.getObject())
                .pageSize(50)
                .rowMapper(new FilmRowMapper())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<FilmDTO> fileWriter(@Value("#{jobParameters['outputFile']}") String outputFile
    ) throws IOException {
        Path p = Paths.get(outputFile).toAbsolutePath();
        Files.createDirectories(p.getParent());
        return new FlatFileItemWriterBuilder<FilmDTO>()
                .name("fileWriter")
                .resource(new FileSystemResource(outputFile))
                .headerCallback(w -> w.write("id,title,year,duration,synopsis,posterRoute"))
                .delimited()
                .delimiter(",")
                .names("id","title","year","duration","synopsis","posterRoute")
                .shouldDeleteIfEmpty(false)
                .append(true)
                .build();
    }

    @Bean
    public Step exportFilmsStep(FlatFileItemWriter<FilmDTO> fileWriter) throws Exception {
        return new StepBuilder("exportFilmsStep", jobRepository)
                .<FilmDTO, FilmDTO>chunk(50, txManager)
                .reader(filmReader())
                .writer(fileWriter)
                .listener(stepListener)
                .listener(writeListener)
                .build();
    }

    @Bean(name = "exportFilmsJob")
    public Job exportFilmsJob(FlatFileItemWriter<FilmDTO> fileWriter) throws Exception {

        return new JobBuilder("exportFilmsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .start(exportFilmsStep(fileWriter))
                .build();
    }

    @Bean
    DataSourceInitializer batchSchemaInitializer(DataSource dataSource) {
        var populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-h2.sql"));
        var initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @PostConstruct
    public void checkDb() throws Exception {
        try (var c = dataSource.getConnection()) {


            try (var st = c.createStatement();
                 var rs = st.executeQuery("SELECT COUNT(*) FROM film WHERE migrate = FALSE")) {
                rs.next();

            }


            try (var st = c.createStatement();
                 var rs = st.executeQuery("SELECT film_id, film_title, migrate FROM film")) {
                while (rs.next()) {
                    System.out.println("🎬 Film ID=" + rs.getInt("film_id")
                            + " | title=" + rs.getString("film_title")
                            + " | migrate=" + rs.getString("migrate"));
                }
            }
        }
    }




}



