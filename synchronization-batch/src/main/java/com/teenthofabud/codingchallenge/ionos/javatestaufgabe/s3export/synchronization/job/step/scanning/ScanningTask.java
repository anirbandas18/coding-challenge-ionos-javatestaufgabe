package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.scanning;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.LandCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.KundeAuftragCollectionRepository;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.LandCollectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@StepScope
public class ScanningTask implements Tasklet {

    private String kundeAuftragCollectionKeyName;
    private KundeAuftragCollectionRepository kundeAuftragCollectionRepository;
    private LandCollectionRepository landCollectionRepository;
    private StepExecution stepExecution;
    private String distinctLandCollectionKeyName;
    private String jobParameterName1;
    private String keyNameDelimitter;

    @Value("${s3export.sync.distinctlandcollection.key.name:distinctLandCollectionKey}")
    public void setDistinctLandCollectionKeyName(String distinctLandCollectionKeyName) {
        this.distinctLandCollectionKeyName = distinctLandCollectionKeyName;
    }

    @Value("${s3export.sync.kundeauftragcollection.key.name:kundeAuftragCollectionKey}")
    public void setKundeAuftragCollectionKeyName(String kundeAuftragCollectionKeyName) {
        this.kundeAuftragCollectionKeyName = kundeAuftragCollectionKeyName;
    }

    @Value("${s3export.sync.job.parameter.1:synchronization-timestamp}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }

    @Value("${s3export.sync.delimitter.key.name:~}")
    public void setKeyNameDelimitter(String keyNameDelimitter) {
        this.keyNameDelimitter = keyNameDelimitter;
    }

    @Autowired
    public void setKundeAuftragCollectionRepository(KundeAuftragCollectionRepository kundeAuftragCollectionRepository) {
        this.kundeAuftragCollectionRepository = kundeAuftragCollectionRepository;
    }

    @Autowired
    public void setLandCollectionRepository(LandCollectionRepository landCollectionRepository) {
        this.landCollectionRepository = landCollectionRepository;
    }

    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    private Optional<String> getKundeAuftragCollectionKeyValue() {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        Object value = jobContext.get(kundeAuftragCollectionKeyName);
        if(value != null) {
            String keyName = value.toString();
            return Optional.of(keyName);
        } else {
            return Optional.empty();
        }
    }

    private String getLandCollectionKeyName() {
        String stepName = stepExecution.getStepName();
        JobExecution jobExecution = stepExecution.getJobExecution();
        String jobId = String.valueOf(jobExecution.getJobId());
        String jobInstanceName = jobExecution.getJobInstance().getJobName();
        String jobInstanceId = String.valueOf(jobExecution.getJobInstance().getInstanceId());
        String jobExecutionId = String.valueOf(jobExecution.getId());
        JobParameters jobParameters = stepExecution.getJobParameters();
        Map<String, JobParameter> parametersMap = jobParameters.getParameters();
        JobParameter parameter = parametersMap.get(jobParameterName1);
        String jobParameterValue1 = parameter.getValue().toString();
        String keyValue = String.join(keyNameDelimitter, Arrays.asList(jobId, jobInstanceName, jobInstanceId, jobExecutionId, jobParameterValue1, stepName));
        return keyValue;
    }

    /**
     * Retrieved the list of kunde auftrag collection as per this job execution instance from in memory store to get the list of distinct land available
     * in the data set and store the distinct land data set in the in memory store for availability in the next step of this batch job
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Optional<String> optKeyValue = getKundeAuftragCollectionKeyValue();
        if(!optKeyValue.isPresent()) {
            // abort because intermediate value for retrieving data of business logic is missing
            log.error("");
        }
        String keyValue = optKeyValue.get();
        KundeAuftragCollectionDto kundeAuftragCollectionDto = kundeAuftragCollectionRepository.findByCollectionKey(keyValue);
        if(kundeAuftragCollectionDto == null || kundeAuftragCollectionDto.getKundeAuftragMap().isEmpty()) {
            // abort because intermediate data set for business logic is missing
            log.error("");
        }
        log.info("Retrieved kunde auftrag collection of size: {} with key: {}", kundeAuftragCollectionDto.getKundeAuftragMap().size(), keyValue);
        List<KundeAuftragDto> kundeAuftragCollection = kundeAuftragCollectionDto.getKundeAuftragMap().stream().map(i -> (KundeAuftragDto) i).collect(Collectors.toList());
        Set<? extends String> distinctLand = kundeAuftragCollection.stream().map(i -> i.getLand()).collect(Collectors.toSet());
        log.info("Retrieved {} distinct land from current kunde auftrag data set and stored against key: {} for use in future downstream step", distinctLand.size(), keyValue);
        String keyName = getLandCollectionKeyName();
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put(keyName, keyValue);
        LandCollectionDto landCollectionDto = new LandCollectionDto();
        landCollectionDto.setCollectionKey(keyValue);
        landCollectionDto.setDistinctLand(distinctLand);
        if(landCollectionRepository.save(landCollectionDto) == null) {
            // abort job because no data will be available for next step to process
            log.error("");
        }
        log.info("Saved distinct collection of size: {} with key: {}", distinctLand.size(), keyValue);
        return RepeatStatus.CONTINUABLE;
    }
}
