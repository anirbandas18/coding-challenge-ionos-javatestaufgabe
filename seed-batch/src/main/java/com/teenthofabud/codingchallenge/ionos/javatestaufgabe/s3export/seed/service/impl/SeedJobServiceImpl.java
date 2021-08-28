package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.service.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.service.SeedJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class SeedJobServiceImpl implements SeedJobService {

    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final AtomicInteger batchRunCounter = new AtomicInteger(0);

    private JobLauncher jobLauncher;
    private Job synchronizationJob;
    private String jobParameterName1;

    @Value("${s3export.seed.job.parameter.1:seed-timestamp}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }


    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setSynchronizationJob(Job synchronizationJob) {
        this.synchronizationJob = synchronizationJob;
    }

    //@Scheduled(cron = "${s3export.seed.job.cron:0 */1 * * * ?}", zone = "${s3export.seed.timezone:Europe/Paris}")
    @Scheduled(cron = "${s3export.seed.job.cron:0 */1 * * * ?}")
    public synchronized void runJob() throws SeedException {
        try {
            //if(enabled.get()) {
                Long synchronizationJobStartTime = System.currentTimeMillis();
                JobParameters synchronizationJobParameters =   new JobParametersBuilder()
                        .addLong(jobParameterName1, synchronizationJobStartTime).toJobParameters();
                log.info("Starting job with parameters: {}", synchronizationJobParameters);
                JobExecution synchronizationJobExecution = jobLauncher.run(synchronizationJob, synchronizationJobParameters);
                batchRunCounter.incrementAndGet();
            //}
        } catch (JobExecutionAlreadyRunningException e) {
            throw new SeedException(e.getMessage());
        } catch (JobRestartException e) {
            throw new SeedException(e.getMessage());
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new SeedException(e.getMessage());
        } catch (JobParametersInvalidException e) {
            throw new SeedException(e.getMessage());
        }
    }

    @Override
    public void startJob() {
        enabled.set(Boolean.TRUE);
    }

    @Override
    public void stopJob() {
        enabled.set(Boolean.FALSE);
    }

}
