package com.FilmFeel.batch;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FilmStepListener {

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        log.info(" Iniciando Step “{}”", stepExecution.getStepName());
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("📊 ReadCount=" + stepExecution.getReadCount());
        System.out.println("📊 WriteCount=" + stepExecution.getWriteCount());
        System.out.println("📊 ExitStatus=" + stepExecution.getExitStatus());
        return stepExecution.getExitStatus();
    }
}

