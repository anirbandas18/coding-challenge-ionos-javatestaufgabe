package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.service.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.service.AuftraegeJobService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedErrorCode;
import com.teenthofabud.core.common.error.TOABSystemException;
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

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class AuftraegeJobServiceImpl implements AuftraegeJobService {

    private final AtomicInteger batchRunCounter = new AtomicInteger(0);

    private JobLauncher jobLauncher;
    private Job auftraegeJob;
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
    public void setAuftraegeJob(Job auftraegeJob) {
        this.auftraegeJob = auftraegeJob;
    }

    /**
     * Run a new instance of this functionality's core job called auftraege every configured amount of time as per the cron expression
     * @throws TOABSystemException
     */
    @Scheduled(cron = "${s3export.seed.job.auftraege.cron:0 */1 * * * ?}")
    @Override
    public synchronized void runJob() throws TOABSystemException {
        try {
            Long synchronizationJobStartTime = System.currentTimeMillis();
            JobParameters synchronizationJobParameters =   new JobParametersBuilder()
                    .addLong(jobParameterName1, synchronizationJobStartTime).toJobParameters();
            log.info("Starting auftraege job with parameters: {}", synchronizationJobParameters);
            JobExecution auftraegeJobExecution = jobLauncher.run(auftraegeJob, synchronizationJobParameters);
            batchRunCounter.incrementAndGet();
            log.info("{} job run number {}", auftraegeJobExecution.getJobInstance().getJobName(), batchRunCounter);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("Error executing auftraege job", e);
            throw new TOABSystemException(SeedErrorCode.SEED_ACTION_FAILURE, "Error executing auftraege job", e);
        }
    }

}
