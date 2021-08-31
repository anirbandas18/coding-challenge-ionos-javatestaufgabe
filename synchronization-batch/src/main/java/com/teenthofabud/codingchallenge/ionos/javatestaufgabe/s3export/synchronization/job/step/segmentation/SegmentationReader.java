package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.KundeAuftragCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.KundeAuftragDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.LandKundenDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.repository.KundeAuftragCollectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SegmentationReader implements ItemReader<List<LandKundenDto>> {

    private String kundeAuftragCollectionKeyName;
    private KundeAuftragCollectionRepository kundeAuftragCollectionRepository;
    private StepExecution stepExecution;

    @Value("${s3export.sync.kundeauftragcollection.key.name:kundeAuftragCollectionKey}")
    public void setKundeAuftragCollectionKeyName(String kundeAuftragCollectionKeyName) {
        this.kundeAuftragCollectionKeyName = kundeAuftragCollectionKeyName;
    }

    @Autowired
    public void setKundeAuftragCollectionRepository(KundeAuftragCollectionRepository kundeAuftragCollectionRepository) {
        this.kundeAuftragCollectionRepository = kundeAuftragCollectionRepository;
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

    private List<LandKundenDto> groupKundeAuftragByland(List<KundeAuftragDto> kundeAuftragCollection, Set<String> distinctLand) {
        List<LandKundenDto> landKundenCollection = new ArrayList<>(distinctLand.size());
        for(String land : distinctLand) {
            List<KundeAuftragDto> associatedKundenAuftrag = kundeAuftragCollection.stream().filter(i -> i.getLand().equalsIgnoreCase(land)).collect(Collectors.toList());
            LandKundenDto lkDto = new LandKundenDto();
            lkDto.setLand(land);
            lkDto.setAssociatedKundenAuftrag(associatedKundenAuftrag);
            landKundenCollection.add(lkDto);
            log.debug("Land {} has associations with {} kunde auftrag mapping", land,
                    associatedKundenAuftrag == null || associatedKundenAuftrag.isEmpty() ? 0 :associatedKundenAuftrag.size());
        }
        return landKundenCollection;
    }

    /**
     * Get the unique key value for the kunde auftrag collection at hand from the current running job's execution context via the configured collection key name
     * Retrieve the collection of kunde auftrag map associated with this collection by the collection's key value
     * @return
     * @throws Exception
     * @throws UnexpectedInputException
     * @throws ParseException
     * @throws NonTransientResourceException
     */
    @Override
    public List<LandKundenDto> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Optional<String> optKeyValue = getKundeAuftragCollectionKeyValue();
        if(!optKeyValue.isPresent()) {
            // abort because intermediate value for retrieving data of business logic is missing
            log.error("");
        }
        String keyValue = optKeyValue.get();
        KundeAuftragCollectionDto kundeAuftragCollectionDto = kundeAuftragCollectionRepository.findByCollectionKey(keyValue);
        log.info("Retrieved kunde auftrag map collection of size: {} with key: {}", kundeAuftragCollectionDto.getKundeAuftragMap().size(), keyValue);
        List<KundeAuftragDto> kundeAuftragCollection = kundeAuftragCollectionDto.getKundeAuftragMap().stream().map(i -> (KundeAuftragDto) i).collect(Collectors.toList());
        Set<String> distinctLand = kundeAuftragCollection.stream().map(i -> i.getLand()).collect(Collectors.toSet());
        log.info("Retrieved {} distinct land from current kunde auftrag data set and stored against key: {} for use in future downstream step", distinctLand.size(), keyValue);
        List<LandKundenDto> landKundenCollection = groupKundeAuftragByland(kundeAuftragCollection, distinctLand);
        return landKundenCollection;
    }
}
