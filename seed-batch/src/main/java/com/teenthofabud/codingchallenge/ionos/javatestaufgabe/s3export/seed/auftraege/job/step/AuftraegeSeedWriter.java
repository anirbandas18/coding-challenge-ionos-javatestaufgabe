package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy.AuftraegeServiceClient;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.converter.AuftraegeModelEntity2FormConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class AuftraegeSeedWriter implements ItemWriter<AuftraegeModelEntity> {

    private AuftraegeModelEntity2FormConverter entity2FormConverter;
    private AuftraegeServiceClient client;

    @Autowired
    public void setClient(AuftraegeServiceClient client) {
        this.client = client;
    }

    @Autowired
    public void setEntity2FormConverter(AuftraegeModelEntity2FormConverter entity2FormConverter) {
        this.entity2FormConverter = entity2FormConverter;
    }

    @Override
    public void write(List<? extends AuftraegeModelEntity> items) throws Exception {
        int count = 0;
        for(AuftraegeModelEntity entity : items) {
            AuftraegeModelForm form = entity2FormConverter.convert(entity);
            String auftragId = client.postNewAuftraegeModel(form);
            if(auftragId.equalsIgnoreCase(form.getAuftragId())) {
                log.debug("POST {} to {} with id: {}", entity, form, auftragId);
                count++;
            } else {
                log.debug("POST {} to {} with id: {} was not successful as expected and actual auftrag ids differ", entity, form, auftragId);
            }
        }
        log.info("Posted {} auftraege forms", count);
    }
}
