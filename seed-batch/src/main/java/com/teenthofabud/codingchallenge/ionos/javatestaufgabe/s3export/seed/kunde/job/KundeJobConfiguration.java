package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job;

import com.github.javafaker.Faker;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step.KundeSeedProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step.KundeSeedReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step.KundeSeedTask;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step.KundeSeedWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KundeJobConfiguration {

    private Integer batchSize;
    private String appName;
    private StepBuilderFactory stepBuilderFactory;
    private JobBuilderFactory jobBuilderFactory;

    @Value("${s3export.seed.batch.kunde.size:50}")
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    @Value("${spring.application.name:seed-job}")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    @StepScope
    public KundeSeedReader kundeSeedReader() {
        return new KundeSeedReader();
    }

    @Bean
    @StepScope
    public KundeSeedProcessor kundeSeedProcessor() {
        return new KundeSeedProcessor();
    }

    @Bean
    @StepScope
    public KundeSeedWriter kundeSeedWriter() {
        return new KundeSeedWriter();
    }

    @Bean
    @StepScope
    public KundeSeedTask kundeSeedTask() {
        return new KundeSeedTask();
    }

    @Bean
    public Step kundeSeeding() {
        return stepBuilderFactory.get("kundeSeeding")
                .tasklet(kundeSeedTask())
                .build();
    }

    @Bean
    public Job kundeJob() {
        return jobBuilderFactory.get("kunde-" + appName)
                .start(kundeSeeding())
                .build();
    }

    @Bean
    public Faker faker() {
        return new Faker();
    }

}
