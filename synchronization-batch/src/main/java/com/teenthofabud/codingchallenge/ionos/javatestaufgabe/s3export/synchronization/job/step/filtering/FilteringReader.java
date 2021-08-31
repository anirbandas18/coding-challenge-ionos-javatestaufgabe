package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.proxy.AuftraegeServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
public class FilteringReader implements ItemReader<List<AuftraegeModelVo>> {

    private AuftraegeServiceClient client;

    private Long synchronizationIntervalAmount;
    private String synchronizationIntervalUnit;

    @Value("${s3export.sync.interval.amount:3}")
    public void setSynchronizationIntervalAmount(Long synchronizationIntervalAmount) {
        this.synchronizationIntervalAmount = synchronizationIntervalAmount;
    }

    @Value("${s3export.sync.interval.unit:hour}")
    public void setSynchronizationIntervalUnit(String synchronizationIntervalUnit) {
        this.synchronizationIntervalUnit = synchronizationIntervalUnit;
    }

    @Autowired
    public void setClient(AuftraegeServiceClient client) {
        this.client = client;
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
    public List<AuftraegeModelVo> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("Request all auftraege within the last {} {}(s) as per UTC time", synchronizationIntervalAmount, synchronizationIntervalUnit);
        List<AuftraegeModelVo> auftraegeModelVoList = client.getAuftraegeModelDetailsWithinTheLastNTime(synchronizationIntervalAmount.toString(), synchronizationIntervalUnit);
        log.info("Retrieved {} auftraege within the last {} {}(s) as per UTC time", auftraegeModelVoList.size(),  synchronizationIntervalAmount, synchronizationIntervalUnit);
        return auftraegeModelVoList;
    }
}
