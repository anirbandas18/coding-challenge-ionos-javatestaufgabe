package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.AuftragKundeCollectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@StepScope
public class FilteringWriter implements ItemWriter<AuftragKundeDto> {

    private StepExecution stepExecution;
    private AuftragKundeCollectionRepository repository;
    private String jobParameterName1;
    private String auftragKundeCollectionKeyName;
    private String keyNameDelimitter;

    @Value("${s3export.sync.delimitter.key.name:~}")
    public void setKeyNameDelimitter(String keyNameDelimitter) {
        this.keyNameDelimitter = keyNameDelimitter;
    }

    @Value("${s3export.sync.auftragkundecollection.key.name:auftragKundeCollectionKey}")
    public void setAuftragKundeCollectionKeyName(String auftragKundeCollectionKeyName) {
        this.auftragKundeCollectionKeyName = auftragKundeCollectionKeyName;
    }

    @Autowired
    public void setRepository(AuftragKundeCollectionRepository repository) {
        this.repository = repository;
    }

    @Value("${s3export.sync.job.parameter.2:synchronization-timestamp}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }

    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }


    /**
     * Create a unique key for the collection at hand using the current running job's execution identifiers and step name
     * Associate the list of auftrag kunde mapping into a collection and assign the generated key
     * Save the key into an in memory datastore for accessibility in the next step of the job
     * Associate the generated key with the job context so that the next step in the job can retrieve the key to access the original data set
     * @param items
     * @throws Exception
     */
    @Override
    public void write(List<? extends AuftragKundeDto> items) throws Exception {
        String keyValue = getAuftragKundeCollectionKeyName();
        log.info("Passing down auftrag kunde collection key value: {} for next step to access it via key name: {} and use it for further processing",
                keyValue, auftragKundeCollectionKeyName);
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put(auftragKundeCollectionKeyName, keyValue);
        AuftragKundeCollectionDto auftragKundeCollectionDto = new AuftragKundeCollectionDto();
        auftragKundeCollectionDto.setCollectionKey(keyValue);
        auftragKundeCollectionDto.setAuftragKundeMap(items);
        if(repository.save(auftragKundeCollectionDto) == null) {
            // abort job because no data will be available for next step to process
            log.error("");
        }
        log.info("Saved AuftragKundeCollection of size: {} with key: {}", items.size(), keyValue);
    }

    private String getAuftragKundeCollectionKeyName() {
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

}