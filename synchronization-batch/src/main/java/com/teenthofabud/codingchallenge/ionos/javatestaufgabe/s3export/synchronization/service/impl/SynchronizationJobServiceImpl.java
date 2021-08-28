package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.service.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.SynchronizationJobDetailVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.error.SynchronizationException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.service.SynchronizationJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SynchronizationJobServiceImpl implements SynchronizationJobService, InitializingBean {

    private JobLauncher jobLauncher;
    private Job synchronizationJob;
    private String jobParameterName1;
    private Map<String, JobParameter> synchronizationJobParameterMap;
    private SimpleDateFormat sdf;
    private String timestampFormat;

    @Value("${s3export.sync.job.timestamp.format:YYYY-MM-dd_HH-mm-ss}")
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }


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

    @Scheduled(cron = "${s3export.sync.job.cron:0 */3 * * * *}", zone = "${s3export.sync.timezone:Europe/Paris}")
    public synchronized void runJob() throws SynchronizationException {
        try {
                Long synchronizationJobStartTime = System.currentTimeMillis();
                JobParameters synchronizationJobParameters =   new JobParametersBuilder()
                            .addLong(jobParameterName1, synchronizationJobStartTime).toJobParameters();
                log.info("Starting job with parameters: {}", synchronizationJobParameters);
                JobExecution synchronizationJobExecution = jobLauncher.run(synchronizationJob, synchronizationJobParameters);
                SynchronizationJobDetailVo vo = new SynchronizationJobDetailVo();
                vo.setId(synchronizationJobExecution.getJobId());
                vo.setExecutionId(synchronizationJobExecution.getId());
                vo.setName(synchronizationJobExecution.getJobInstance().getJobName());
                vo.setInstanceId(synchronizationJobExecution.getJobInstance().getInstanceId());
                Date synchronizationJobStartDate = new Date();
                synchronizationJobStartDate.setTime(synchronizationJobStartTime);
                vo.setCreateTime(sdf.format(synchronizationJobStartDate));
                vo.setStartTime(sdf.format(synchronizationJobExecution.getStartTime()));
                vo.setStatus(synchronizationJobExecution.getStatus().getBatchStatus().name());
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

    @Override
    public void afterPropertiesSet() throws Exception {
        synchronizationJobParameterMap = new HashMap<>();
        synchronizationJobParameterMap.put(jobParameterName1, new JobParameter(new Date()));
        sdf = new SimpleDateFormat(timestampFormat);
    }
}
