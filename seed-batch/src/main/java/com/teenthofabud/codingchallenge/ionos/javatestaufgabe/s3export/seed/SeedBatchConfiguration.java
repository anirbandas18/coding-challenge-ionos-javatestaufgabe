package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.job.step.SeedProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.job.step.SeedReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.job.step.SeedTask;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.job.step.SeedWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableBatchProcessing
@EnableScheduling
@EnableJpaRepositories
@Slf4j
public class SeedBatchConfiguration {

    private Integer batchSize;
    private String appName;
    private StepBuilderFactory stepBuilderFactory;
    private JobBuilderFactory jobBuilderFactory;

    @Value("${s3export.seed.batch.size:10}")
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
    public SeedReader seedReader() {
        return new SeedReader();
    }

    @Bean
    @StepScope
    public SeedProcessor seedProcessor() {
        return new SeedProcessor();
    }

    @Bean
    @StepScope
    public SeedWriter seedWriter() {
        return new SeedWriter();
    }

    @Bean
    @StepScope
    public SeedTask seedTask() {
        return new SeedTask();
    }

    @Bean
    public Step seeding() {
        return stepBuilderFactory.get("seeding")
                .tasklet(seedTask())
                .build();
    }

    @Bean
    public Job seedJob() {
        return jobBuilderFactory.get(appName)
                .start(seeding())
                .build();
    }

}
