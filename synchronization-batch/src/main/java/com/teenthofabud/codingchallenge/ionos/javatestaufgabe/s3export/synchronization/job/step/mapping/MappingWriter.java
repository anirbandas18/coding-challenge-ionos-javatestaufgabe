package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.KundeAuftragCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.KundeAuftragDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.repository.KundeAuftragCollectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class MappingWriter implements ItemWriter<List<KundeAuftragDto>> {

    private StepExecution stepExecution;
    private String jobParameterName1;
    private String kundeAuftragCollectionKeyName;
    private String keyNameDelimitter;
    private KundeAuftragCollectionRepository repository;

    @Autowired
    public void setRepository(KundeAuftragCollectionRepository repository) {
        this.repository = repository;
    }

    @Value("${s3export.sync.delimitter.key.name:~}")
    public void setKeyNameDelimitter(String keyNameDelimitter) {
        this.keyNameDelimitter = keyNameDelimitter;
    }

    @Value("${s3export.sync.kundeauftragcollection.key.name:kundeAuftragCollectionKey}")
    public void setKundeAuftragCollectionKeyName(String kundeAuftragCollectionKeyName) {
        this.kundeAuftragCollectionKeyName = kundeAuftragCollectionKeyName;
    }

    @Value("${s3export.sync.job.parameter.1:synchronization-timestamp}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }

    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    private String getKundeAuftragCollectionKeyValue() {
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
     * Create a unique key for the collection at hand using the current running job's execution identifiers and step name
     * Associate the list of kunde auftrag mapping into a collection and assign the generated key
     * Save the key into an in memory datastore for accessibility in the next step of the job
     * Associate the generated key with the job context so that the next step in the job can retrieve the key to access the original data set
     * @param items
     * @throws Exception
     */
    @Override
    public void write(List<? extends List<KundeAuftragDto>> items) throws Exception {
        List<KundeAuftragDto> kundeAuftragDtoList = items.get(0);
        String keyValue = getKundeAuftragCollectionKeyValue();
        log.info("Passing down kunde auftrag collection key value: {} for next step to access it via key name: {} and use it for further processing",
                keyValue, kundeAuftragCollectionKeyName);
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put(kundeAuftragCollectionKeyName, keyValue);
        KundeAuftragCollectionDto kundeAuftragCollectionDto = new KundeAuftragCollectionDto();
        kundeAuftragCollectionDto.setCollectionKey(keyValue);
        kundeAuftragCollectionDto.setKundeAuftragMap(kundeAuftragDtoList);
        repository.save(kundeAuftragCollectionDto);
        log.info("Saved KundeAuftragCollection of size: {} with key: {}", kundeAuftragDtoList.size(), keyValue);
    }
}
