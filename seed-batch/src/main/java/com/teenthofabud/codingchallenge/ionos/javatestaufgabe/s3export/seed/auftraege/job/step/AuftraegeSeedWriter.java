package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.error.AuftraegeSeedException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy.AuftraegeServiceClient;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.converter.AuftraegeModelEntity2FormConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
public class AuftraegeSeedWriter implements ItemWriter<AuftraegeModelEntity>, InitializingBean {

    private AuftraegeModelEntity2FormConverter entity2FormConverter;
    private AuftraegeServiceClient client;
    private CircuitBreakerFactory globalCircuitBreakerFactory;

    private CircuitBreaker auftraegeServiceCircuitBreaker;

    @Autowired
    public void setGlobalCircuitBreakerFactory(CircuitBreakerFactory globalCircuitBreakerFactory) {
        this.globalCircuitBreakerFactory = globalCircuitBreakerFactory;
    }

    @Autowired
    public void setClient(AuftraegeServiceClient client) {
        this.client = client;
    }

    @Autowired
    public void setEntity2FormConverter(AuftraegeModelEntity2FormConverter entity2FormConverter) {
        this.entity2FormConverter = entity2FormConverter;
    }

    private String defaultPostNewAuftraegeModel(AuftraegeModelForm form) {
        log.debug("Circuit breaker triggered for Auftraege service client");
        return "";
    }

    @Override
    public void write(List<? extends AuftraegeModelEntity> items) throws Exception {
        int count = 0;
        for(AuftraegeModelEntity entity : items) {
            AuftraegeModelForm form = entity2FormConverter.convert(entity);
            String auftragId = auftraegeServiceCircuitBreaker.run(() -> client.postNewAuftraegeModel(form),
                    throwable -> defaultPostNewAuftraegeModel(form));
            if(StringUtils.isEmpty(StringUtils.trimWhitespace(auftragId))) {
                log.error("Auftrage service not responsive");
                throw new AuftraegeSeedException(SeedErrorCode.SEED_ACTION_FAILURE, new Object[] { "Auftrage service not responsive" });
            }
            if(auftragId.equalsIgnoreCase(form.getAuftragId())) {
                log.debug("POST {} to {} with id: {}", entity, form, auftragId);
                count++;
            } else {
                log.debug("POST {} to {} with id: {} was not successful as expected and actual auftrag ids differ", entity, form, auftragId);
            }
        }
        log.info("Posted {} auftraege forms", count);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.auftraegeServiceCircuitBreaker = globalCircuitBreakerFactory.create("auftraege");
    }
}
