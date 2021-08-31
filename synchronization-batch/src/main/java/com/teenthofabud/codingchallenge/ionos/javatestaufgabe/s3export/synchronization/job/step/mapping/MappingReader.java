package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.AuftragKundeCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.repository.AuftragKundeCollectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MappingReader implements ItemReader<List<AuftragKundeDto>> {

    private AuftragKundeCollectionRepository collectionRepository;
    private StepExecution stepExecution;
    private String auftragKundeCollectionKeyName;

    @Value("${s3export.sync.auftragkundecollection.key.name:auftragKundeCollectionKey}")
    public void setAuftragKundeCollectionKeyName(String auftragKundeCollectionKeyName) {
        this.auftragKundeCollectionKeyName = auftragKundeCollectionKeyName;
    }

    @Autowired
    public void setCollectionRepository(AuftragKundeCollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    private Optional<String> getAuftragKundeCollectionKeyValue() {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        Object value = jobContext.get(auftragKundeCollectionKeyName);
        if(value != null) {
            String keyName = value.toString();
            return Optional.of(keyName);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the unique key value for the auftrag kunde collection at hand from the current running job's execution context via the configured collection key name
     * Retrieve the collection of auftrag-kunde map associated with this collection by the collection's key value
     * @return
     * @throws Exception
     * @throws UnexpectedInputException
     * @throws ParseException
     * @throws NonTransientResourceException
     */
    @Override
    public List<AuftragKundeDto> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Optional<String> optKeyValue = getAuftragKundeCollectionKeyValue();
        if(!optKeyValue.isPresent()) {
            // abort because intermediate value for retrieving data of business logic is missing
            log.error("");
        }
        String keyValue = optKeyValue.get();
        AuftragKundeCollectionDto auftragKundeCollectionDto = collectionRepository.findByCollectionKey(keyValue);
        log.info("Retrieved auftrag kunde map collection of size: {} with key: {}", auftragKundeCollectionDto.getAuftragKundeMap().size(), keyValue);
        List<AuftragKundeDto> auftragKundeMap = auftragKundeCollectionDto.getAuftragKundeMap().stream().map(i -> (AuftragKundeDto) i).collect(Collectors.toList());
        return auftragKundeMap;
    }

}
