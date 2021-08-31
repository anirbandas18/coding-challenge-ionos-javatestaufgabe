/*
package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.validator;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.error.AuftraegeException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy.AuftraegeServiceClient;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class AuftraegeModelValidator implements Validator {
    @Autowired
    public void setKundeServiceClient(AuftraegeServiceClient auftraegeServiceClient) {
        this.auftraegeServiceClient = auftraegeServiceClient;
    }

    private AuftraegeServiceClient auftraegeServiceClient;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Long.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String auftragId = (String) target;

        try {
            AuftraegeModelVo auftraegeModelVo = auftraegeServiceClient.getAuftraegeModelDetailsByAuftragId(auftragId);
            if(StringUtils.hasText(StringUtils.trimWhitespace(auftraegeModelVo.getKundenId()))) {
                log.debug("auftraegeModelVo.auftragId is empty");
                errors.reject(SeedErrorCode.SEED_ATTRIBUTE_INVALID.name());
                return;
            } else if (!auftraegeModelVo.getKundenId().equals(auftragId)) {
                log.debug("auftraegeModelVo.kundenId doesn't match with registered value");
                errors.reject(SeedErrorCode.SEED_ATTRIBUTE_INVALID.name());
                return;
            }
        } catch (AuftraegeException e) {
            log.debug("auftragId is invalid");
            log.error("auftragId is invalid", e);
            errors.reject(SeedErrorCode.SEED_ATTRIBUTE_INVALID.name());
            return;
        }
    }
}
*/
