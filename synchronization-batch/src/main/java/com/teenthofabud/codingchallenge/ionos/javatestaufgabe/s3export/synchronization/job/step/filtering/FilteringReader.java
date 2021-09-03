package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.proxy.AuftraegeServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FilteringReader implements ItemReader<List<AuftraegeModelVo>>, InitializingBean {

    private AuftraegeServiceClient client;

    private CircuitBreakerFactory globalCircuitBreakerFactory;

    private CircuitBreaker auftraegeServiceCircuitBreaker;

    @Autowired
    public void setGlobalCircuitBreakerFactory(CircuitBreakerFactory globalCircuitBreakerFactory) {
        this.globalCircuitBreakerFactory = globalCircuitBreakerFactory;
    }

    private Long synchronizationIntervalAmount;
    private String synchronizationIntervalUnit;

    @Value("${s3export.sync.interval.amount:3}")
    public void setSynchronizationIntervalAmount(Long synchronizationIntervalAmount) {
        this.synchronizationIntervalAmount = synchronizationIntervalAmount;
    }

    @Value("${s3export.sync.interval.unit:minute}")
    public void setSynchronizationIntervalUnit(String synchronizationIntervalUnit) {
        this.synchronizationIntervalUnit = synchronizationIntervalUnit;
    }

    @Autowired
    public void setClient(AuftraegeServiceClient client) {
        this.client = client;
    }

    private List<AuftraegeModelVo> defaultGetAuftraegeModelDetailsWithinTheLastNTime(String amount, String unit) {
        log.debug("Circuit breaker triggered for Auftraege service client");
        return new LinkedList<>();
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
        List<AuftraegeModelVo> auftraegeModelVoList = auftraegeServiceCircuitBreaker.run(
                () -> client.getAuftraegeModelDetailsWithinTheLastNTime(synchronizationIntervalAmount.toString(), synchronizationIntervalUnit),
                throwable -> defaultGetAuftraegeModelDetailsWithinTheLastNTime(synchronizationIntervalAmount.toString(), synchronizationIntervalUnit));
        // TODO analyze List<AuftraegeModelVo> auftraegeModelVoList to ascertain whether this an exception should be throw or not for skipping
        log.info("Retrieved {} auftraege within the last {} {}(s) as per UTC time", auftraegeModelVoList.size(),  synchronizationIntervalAmount, synchronizationIntervalUnit);
        return auftraegeModelVoList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.auftraegeServiceCircuitBreaker = globalCircuitBreakerFactory.create("auftraege");
    }
}
