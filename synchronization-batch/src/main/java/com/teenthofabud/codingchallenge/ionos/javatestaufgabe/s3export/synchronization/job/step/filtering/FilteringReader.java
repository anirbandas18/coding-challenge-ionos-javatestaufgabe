package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.AuftraegeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@StepScope
public class FilteringReader implements ItemReader<List<AuftraegeEntity>> {

    private AuftraegeRepository repository;

    @Value("${s3export.sync.interval.in.hours:3}")
    private Long synchronizationIntervalInHours;

    @Autowired
    public void setRepository(AuftraegeRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all auftraege within between the now and last synchronization event that took place
     * If no matching data is available, the job is aborted
     * @return List of auftraege satisfying the filter condition
     * @throws Exception
     * @throws UnexpectedInputException
     * @throws ParseException
     * @throws NonTransientResourceException
     */
    @Override
    public List<AuftraegeEntity> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("Request all auftraege within the last {} hour(s) as per UTC time", synchronizationIntervalInHours);
        List<AuftraegeEntity> filteredAuftraege = repository.findAllWithinTheLastNHourFromNowAsPerUTC(synchronizationIntervalInHours);
        if(filteredAuftraege == null || filteredAuftraege.isEmpty()) {
            // abort job because no starting data found
            log.error("");
        }
        log.info("Retrieved {} auyftraege within the last {} hour(s) as per UTC time", filteredAuftraege.size());
        return filteredAuftraege;
    }
}
