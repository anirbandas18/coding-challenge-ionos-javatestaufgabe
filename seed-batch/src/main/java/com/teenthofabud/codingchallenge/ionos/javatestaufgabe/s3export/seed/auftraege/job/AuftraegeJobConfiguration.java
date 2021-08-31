package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step.AuftraegeSeedProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step.AuftraegeSeedReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step.AuftraegeSeedTask;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step.AuftraegeSeedWriter;
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
public class AuftraegeJobConfiguration {

    private Integer batchSize;
    private String appName;
    private StepBuilderFactory stepBuilderFactory;
    private JobBuilderFactory jobBuilderFactory;

    @Value("${s3export.seed.batch.auftraege.size:10}")
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
    public AuftraegeSeedReader auftraegeSeedReader() {
        return new AuftraegeSeedReader();
    }

    @Bean
    @StepScope
    public AuftraegeSeedProcessor auftraegeSeedProcessor() {
        return new AuftraegeSeedProcessor();
    }

    @Bean
    @StepScope
    public AuftraegeSeedWriter auftraegeSeedWriter() {
        return new AuftraegeSeedWriter();
    }

    @Bean
    @StepScope
    public AuftraegeSeedTask auftraegeSeedTask() {
        return new AuftraegeSeedTask();
    }

    @Bean
    public Step auftraegeSeeding() {
        return stepBuilderFactory.get("auftraegeSeeding")
                .tasklet(auftraegeSeedTask())
                .build();
    }

    @Bean
    public Job auftraegeJob() {
        return jobBuilderFactory.get("auftraege-" + appName)
                .start(auftraegeSeeding())
                .build();
    }

}
