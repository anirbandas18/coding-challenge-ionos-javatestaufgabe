package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.service.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.service.KundeJobService;
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
public class KundeJobServiceImpl implements KundeJobService {

    private final AtomicInteger batchRunCounter = new AtomicInteger(0);

    private JobLauncher jobLauncher;
    private Job kundeJob;
    private String jobParameterName1;

    @Value("${s3export.seed.job.parameter.1}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }


    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setKundeJob(Job kundeJob) {
        this.kundeJob = kundeJob;
    }

    /**
     * Run a new instance of this functionality's core job called kunde every configured amount of time as per the cron expression
     * @throws TOABSystemException
     */
    @Scheduled(cron = "${s3export.seed.job.kunde.cron}")
    @Override
    public void runJob() throws TOABSystemException {
        try {
            Long synchronizationJobStartTime = System.currentTimeMillis();
            JobParameters synchronizationJobParameters =   new JobParametersBuilder()
                    .addLong(jobParameterName1, synchronizationJobStartTime).toJobParameters();
            log.info("Starting kunde job with parameters: {}", synchronizationJobParameters);
            JobExecution kundeJobExecution = jobLauncher.run(kundeJob, synchronizationJobParameters);
            batchRunCounter.incrementAndGet();
            log.info("{} job run number {}", kundeJobExecution.getJobInstance().getJobName(), batchRunCounter);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("Error executing kunde job", e);
            throw new TOABSystemException(SeedErrorCode.SEED_ACTION_FAILURE, "Error executing kunde job", e);
        }
    }

}
