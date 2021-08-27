package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.KundeAuftragCollectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
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
public class MappingWriter implements ItemWriter<KundeAuftragDto> {

    private static final String FILE_NAME_DELIMITTER = "+";
    private static final String FILE_EXTENSION_DELIMITTER = ".";

    private StepExecution stepExecution;
    private String jobParameterName1;
    private String timestampFormat;
    private String fileExtension;
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

    @Value("${s3export.sync.export.file.extension:csv}")
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Value("${s3export.sync.job.timestamp.format:YYYY-MM-dd_HH-mm-ss}")
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    @Value("${s3export.sync.job.parameter.1:synchronization-timestamp}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }

    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    /*private String getJobExecutionTimestamp() throws ParseException {
        JobParameters jobParameters = stepExecution.getJobParameters();
        Map<String, JobParameter> parametersMap = jobParameters.getParameters();
        JobParameter parameter = parametersMap.get(jobParameterName1);
        String jobParameterValue1 = parameter.getValue().toString();
        SimpleDateFormat sdf = new SimpleDateFormat(timestampFormat);
        Date jobDate = sdf.parse(jobParameterValue1);
        String jobTimeStamp = sdf.format(jobDate);
        return jobTimeStamp;
    }

    private String getFileName(String country, String timestamp) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(country);
        sbf.append(FILE_NAME_DELIMITTER);
        sbf.append(timestamp);
        sbf.append(FILE_EXTENSION_DELIMITTER);
        sbf.append(fileExtension);
        return sbf.toString();
    }*/

    private String getKundeAuftragCollectionKeyName() {
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
    public void write(List<? extends KundeAuftragDto> items) throws Exception {
        String keyValue = getKundeAuftragCollectionKeyName();
        log.info("Passing down kunde auftrag collection key value: {} for next step to access it via key name: {} and use it for further processing",
                keyValue, kundeAuftragCollectionKeyName);
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put(kundeAuftragCollectionKeyName, keyValue);
        KundeAuftragCollectionDto kundeAuftragCollectionDto = new KundeAuftragCollectionDto();
        kundeAuftragCollectionDto.setCollectionKey(keyValue);
        kundeAuftragCollectionDto.setKundeAuftragMap(items);
        if(repository.save(kundeAuftragCollectionDto) == null) {
            // abort job because no data will be available for next step to process
            log.error("");
        }
        log.info("Saved KundeAuftragCollection of size: {} with key: {}", items.size(), keyValue);
    }
}
