package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditForm;
import com.teenthofabud.core.common.handler.TOABBaseEntityAuditHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditForm2EntityConverter extends TOABBaseEntityAuditHandler implements Converter<AuditForm, AuditEntity> {

    @Override
    public AuditEntity convert(AuditForm form) {
        AuditEntity entity = new AuditEntity();
        entity.setAction(form.getAction());
        entity.setModule(form.getModule());
        entity.setDescription(form.getDescription());
        super.assignAuditValues(entity, Boolean.TRUE);
        log.debug("Converting {} to {}", form, entity);
        return entity;
    }
}
