package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.service.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.error.SynchronizationException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.service.SynchronizationJobService;
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

import java.time.Instant;

@Component
@Slf4j
public class SynchronizationJobServiceImpl implements SynchronizationJobService {

    private JobLauncher jobLauncher;
    private Job synchronizationJob;
    private String jobParameterName1;

    @Value("${s3export.sync.job.parameter.1:synchronization-timestamp}")
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

    /**
     * Run a new instance of this functionality's core job every configured amount of time as per the cron expression
     * @throws SynchronizationException
     */
    @Scheduled(cron = "${s3export.sync.job.cron:0 */5 * * * ?}")
    public synchronized void runJob() throws SynchronizationException {
        try {
            Instant synchronizationInstant = Instant.now();
            Long synchronizationJobStartTime = synchronizationInstant.toEpochMilli();;
            JobParameters synchronizationJobParameters =   new JobParametersBuilder()
                .addLong(jobParameterName1, synchronizationJobStartTime).toJobParameters();
            log.info("Starting job with parameters: {}", synchronizationJobParameters);
            JobExecution synchronizationJobExecution = jobLauncher.run(synchronizationJob, synchronizationJobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new SynchronizationException(e.getMessage());
        } catch (JobRestartException e) {
            throw new SynchronizationException(e.getMessage());
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new SynchronizationException(e.getMessage());
        } catch (JobParametersInvalidException e) {
            throw new SynchronizationException(e.getMessage());
        }
    }

}
