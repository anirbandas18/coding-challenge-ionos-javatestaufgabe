package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelPrimaryKey;
import com.teenthofabud.core.common.handler.TOABBaseEntityAuditHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuftraegeModelEntity2FormConverter extends TOABBaseEntityAuditHandler implements Converter<AuftraegeModelEntity, AuftraegeModelForm> {


    @Override
    public AuftraegeModelForm convert(AuftraegeModelEntity entity) {
        AuftraegeModelForm form = new AuftraegeModelForm();
        AuftraegeModelPrimaryKey pk = entity.getPrimaryKey();
        form.setAuftragId(pk.getAuftragId());
        form.setArtikelNummer(pk.getArtikelNummber());
        form.setKundenId(pk.getKundenId());
        form.setCreated(entity.getCreated());
        form.setLastChange(entity.getLastChange());
        log.debug("Converting {} to {}", entity, form);
        return form;
    }

}
