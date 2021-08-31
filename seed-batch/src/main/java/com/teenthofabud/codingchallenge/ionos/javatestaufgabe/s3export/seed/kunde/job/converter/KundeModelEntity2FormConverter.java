package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.data.KundeModelEntity;
import com.teenthofabud.core.common.handler.TOABBaseEntityAuditHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KundeModelEntity2FormConverter extends TOABBaseEntityAuditHandler implements Converter<KundeModelEntity, KundeModelForm> {

    @Override
    public KundeModelForm convert(KundeModelEntity entity) {
        KundeModelForm form = new KundeModelForm();
        form.setEmail(entity.getEmail());
        form.setFirmenName(entity.getFirmenName());
        form.setLand(entity.getLand());
        form.setOrt(entity.getOrt());
        form.setNachName(entity.getNachName());
        form.setPlz(entity.getPlz());
        form.setStrasse(entity.getStrasse());
        form.setVorName(entity.getVorName());
        form.setStrassenZuSatz(entity.getStrassenZuSatz());
        log.debug("Converting {} to {}", entity, form);
        return form;
    }
}
