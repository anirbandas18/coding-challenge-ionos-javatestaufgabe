package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelPrimaryKey;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelForm;
import com.teenthofabud.core.common.handler.TOABBaseEntityAuditHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuftraegeModelForm2EntityConverter extends TOABBaseEntityAuditHandler implements Converter<AuftraegeModelForm, AuftraegeModelEntity> {


    @Override
    public AuftraegeModelEntity convert(AuftraegeModelForm form) {
        AuftraegeModelEntity entity = new AuftraegeModelEntity();
        AuftraegeModelPrimaryKey pk = new AuftraegeModelPrimaryKey();
        pk.setKundenId(form.getKundenId());
        pk.setArtikelNummer(form.getArtikelNummer());
        pk.setAuftragId(form.getAuftragId());
        entity.setPrimaryKey(pk);
        entity.setCreated(form.getCreated());
        entity.setLastChange(form.getLastChange());
        log.debug("Converting {} to {}", form, form);
        return entity;
    }

}
