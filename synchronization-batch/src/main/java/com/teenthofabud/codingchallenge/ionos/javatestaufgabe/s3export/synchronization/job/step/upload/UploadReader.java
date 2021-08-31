package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.upload;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.FileBucketCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.FileBucketDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.repository.FileNameLocationCollectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Slf4j
public class UploadReader implements ItemReader<List<FileBucketDto>> {

    private FileNameLocationCollectionRepository repository;
    private StepExecution stepExecution;
    private String fileBucketCollectionKeyName;

    @Value("${s3export.sync.filebucketcollection.key.name:fileBucketCollectionKey}")
    public void setFileBucketCollectionKeyName(String fileBucketCollectionKeyName) {
        this.fileBucketCollectionKeyName = fileBucketCollectionKeyName;
    }

    @Autowired
    public void setRepository(FileNameLocationCollectionRepository repository) {
        this.repository = repository;
    }

    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    private Optional<String> getFileBucketCollectionKeyValue() {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        Object value = jobContext.get(fileBucketCollectionKeyName);
        if(value != null) {
            String keyName = value.toString();
            return Optional.of(keyName);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the list of file bucket file location map from in memory store as indicated for reception per previous step from this step within the current exeution instance of the job
     * @return
     * @throws Exception
     * @throws UnexpectedInputException
     * @throws ParseException
     * @throws NonTransientResourceException
     */
    @Override
    public List<FileBucketDto> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Optional<String> optKeyValue = getFileBucketCollectionKeyValue();
        if(!optKeyValue.isPresent()) {
            // abort because intermediate value for retrieving data of business logic is missing
            log.error("");
        }
        String keyValue = optKeyValue.get();
        FileBucketCollectionDto fileBucketCollectionDto = repository.findByCollectionKey(keyValue);
        log.info("Retrieved file bucket map collection of size: {} with key: {}", fileBucketCollectionDto.getFileNameLocationMap().size(), keyValue);
        List<FileBucketDto> fileBucketMap = fileBucketCollectionDto == null || fileBucketCollectionDto.getFileNameLocationMap() == null
                ? new LinkedList<>() : fileBucketCollectionDto.getFileNameLocationMap();
        return fileBucketMap;
    }
}
