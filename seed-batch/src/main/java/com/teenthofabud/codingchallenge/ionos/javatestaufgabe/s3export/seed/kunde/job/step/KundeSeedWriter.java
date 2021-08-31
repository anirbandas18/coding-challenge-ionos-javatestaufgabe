package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy.KundeServiceClient;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.converter.KundeModelEntity2FormConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.data.KundeModelEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class KundeSeedWriter implements ItemWriter<KundeModelEntity> {

    private KundeServiceClient client;
    private KundeModelEntity2FormConverter entity2FormConverter;

    @Autowired
    public void setClient(KundeServiceClient client) {
        this.client = client;
    }

    @Autowired
    public void setEntity2FormConverter(KundeModelEntity2FormConverter entity2FormConverter) {
        this.entity2FormConverter = entity2FormConverter;
    }

    @Override
    public void write(List<? extends KundeModelEntity> items) throws Exception {
        int count = 0;
        for(KundeModelEntity entity : items) {
            KundeModelForm form = entity2FormConverter.convert(entity);
            String kundenId = client.postNewKundeModel(form);
            log.debug("POST {} to {} with kunden id {}", entity, form, kundenId);
            count++;
        }
        log.info("Posted {} kunde forms", count);
    }
}
