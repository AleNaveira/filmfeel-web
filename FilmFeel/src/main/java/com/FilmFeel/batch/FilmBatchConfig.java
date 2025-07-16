package com.FilmFeel.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class FilmBatchConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final FilmJobListener jobListener;
    private final FilmItemWriteListener writeListener;

    /** 1) Reader con Paging y RowMapper */
    @Bean
    public JdbcPagingItemReader<FilmDTO> filmReader() throws Exception {
        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(dataSource);
        factory.setSelectClause("SELECT id, title, year, duration, synopsis, poster_route");
        factory.setFromClause("FROM film");
        factory.setWhereClause("WHERE migrate = false");
        factory.setSortKey("id");
        PagingQueryProvider provider = factory.getObject();

        return new JdbcPagingItemReaderBuilder<FilmDTO>()
                .name("filmReader")
                .dataSource(dataSource)
                .queryProvider(provider)
                .pageSize(50)
                .rowMapper(new FilmRowMapper())
                .build();
    }

    /** 2) Writer a CSV */
    @Bean
    @StepScope
    public FlatFileItemWriter<FilmDTO> filmWriter(
            @Value("#{jobParameters['outputFile']}") String outputFile
    ) {
        return new FlatFileItemWriterBuilder<FilmDTO>()
                .name("filmWriter")
                .resource(new FileSystemResource(outputFile))
                .headerCallback(w -> w.write("id,title,year,duration,synopsis,poster_route"))
                .delimited()
                .delimiter(",")
                .names("id","title","year","duration","synopsis","posterRoute")
                .shouldDeleteIfEmpty(false)
                .append(true)
                .build();
    }

    /** 3) Step: chunk, reader, writer y listener2 */
    @Bean
    public Step exportFilmsStep() throws Exception {
        return new StepBuilder("exportFilmsStep", jobRepository)
                .<FilmDTO,FilmDTO>chunk(50, transactionManager)
                .reader(filmReader())
                .writer(filmWriter(null))
                .listener(writeListener)
                .build();
    }

    /** 4) Job: listener1 y step */
    @Bean
    public Job exportFilmsJob() throws Exception {
        return new JobBuilder("exportFilmsJob", jobRepository)
                .incrementer(new org.springframework.batch.core.launch.support.RunIdIncrementer())
                .listener(jobListener)
                .start(exportFilmsStep())
                .build();
    }
}

