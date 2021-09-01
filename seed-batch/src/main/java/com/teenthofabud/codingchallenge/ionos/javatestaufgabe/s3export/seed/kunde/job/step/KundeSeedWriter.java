package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy.KundeServiceClient;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.converter.KundeModelEntity2FormConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.data.KundeModelEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import java.util.List;

@Slf4j
public class KundeSeedWriter implements ItemWriter<KundeModelEntity>, InitializingBean {

    private KundeServiceClient client;
    private KundeModelEntity2FormConverter entity2FormConverter;
    private CircuitBreakerFactory globalCircuitBreakerFactory;

    private CircuitBreaker kundeServiceCircuitBreaker;

    @Autowired
    public void setGlobalCircuitBreakerFactory(CircuitBreakerFactory globalCircuitBreakerFactory) {
        this.globalCircuitBreakerFactory = globalCircuitBreakerFactory;
    }

    @Autowired
    public void setClient(KundeServiceClient client) {
        this.client = client;
    }

    @Autowired
    public void setEntity2FormConverter(KundeModelEntity2FormConverter entity2FormConverter) {
        this.entity2FormConverter = entity2FormConverter;
    }

    private String defaultPostNewKundeModel(KundeModelForm form) {
        log.debug("Circuit breaker triggered for Kunde service client");
        return "";
    }

    @Override
    public void write(List<? extends KundeModelEntity> items) throws Exception {
        int count = 0;
        for(KundeModelEntity entity : items) {
            KundeModelForm form = entity2FormConverter.convert(entity);
            String kundenId = kundeServiceCircuitBreaker.run(() -> client.postNewKundeModel(form),
                    throwable -> defaultPostNewKundeModel(form));
            // TODO analyze kundenId to ascertain whether this an exception should be throw or not for skipping
            log.debug("POST {} to {} with kunden id {}", entity, form, kundenId);
            count++;
        }
        log.info("Posted {} kunde forms", count);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.kundeServiceCircuitBreaker = globalCircuitBreakerFactory.create("kunde");
    }
}
