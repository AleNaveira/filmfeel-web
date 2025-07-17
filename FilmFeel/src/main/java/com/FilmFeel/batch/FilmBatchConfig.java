package com.FilmFeel.batch;

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
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class FilmBatchConfig {

    private final DataSource dataSource;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final FilmJobListener jobListener;
    private final FilmItemWriteListener writeListener;

    @Bean
    public JdbcPagingItemReader<FilmDTO> filmReader() throws Exception {
        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(dataSource);
        factory.setSelectClause(
                "SELECT film_id AS id, film_title AS title, film_year AS year," +
                        " film_duration AS duration, film_synopsis AS synopsis, poster_route AS posterRoute"
        );
        factory.setFromClause("FROM film");
        factory.setWhereClause("WHERE migrate = FALSE");
        factory.setSortKey("film_id");
        var provider = factory.getObject();

        return new JdbcPagingItemReaderBuilder<FilmDTO>()
                .name("filmReader")
                .dataSource(dataSource)
                .queryProvider(provider)
                .pageSize(50)
                .rowMapper(new FilmRowMapper())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<FilmDTO> fileWriter(
            @Value("#{jobParameters['outputFile']}") String outputFile
    ) {
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
    public Step exportFilmsStep() throws Exception {
        return new StepBuilder("exportFilmsStep", jobRepository)
                .<FilmDTO,FilmDTO>chunk(50, txManager)
                .reader(filmReader())
                .writer(fileWriter(null))
                .listener(jobListener)
                .listener(writeListener)
                .build();
    }

    @Bean(name = "filmMigrationJob")
    public Job exportFilmsJob() throws Exception {
        return new JobBuilder("exportFilmsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exportFilmsStep())
                .build();
    }
}
