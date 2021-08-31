package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelForm;
import com.teenthofabud.core.common.handler.TOABBaseEntityAuditHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KundeModelForm2EntityConverter extends TOABBaseEntityAuditHandler implements Converter<KundeModelForm, KundeModelEntity> {

    @Override
    public KundeModelEntity convert(KundeModelForm form) {
        KundeModelEntity entity = new KundeModelEntity();
        entity.setEmail(form.getEmail());
        entity.setFirmenName(form.getFirmenName());
        entity.setLand(form.getLand());
        entity.setOrt(form.getOrt());
        entity.setNachName(form.getNachName());
        entity.setPlz(form.getPlz());
        entity.setStrasse(form.getStrasse());
        entity.setVorName(form.getVorName());
        entity.setStrassenZuSatz(form.getStrassenZuSatz());
        log.debug("Converting {} to {}", form, form);
        return entity;
    }
}
