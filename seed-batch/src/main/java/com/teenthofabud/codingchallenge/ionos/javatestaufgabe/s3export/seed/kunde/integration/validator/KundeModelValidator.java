package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.validator;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.data.KundeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.error.KundeException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy.KundeServiceClient;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class KundeModelValidator implements Validator {
    @Autowired
    public void setKundeServiceClient(KundeServiceClient kundeServiceClient) {
        this.kundeServiceClient = kundeServiceClient;
    }

    private KundeServiceClient kundeServiceClient;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Long.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String kundenId = (String) target;

        try {
            KundeModelVo kundeModelVo = kundeServiceClient.getKundeModelDetailsByKundenId(kundenId);
            if(kundeModelVo.getKundenId() == null) {
                log.debug("kundeModelVo.kundenId is null");
                errors.reject(SeedErrorCode.SEED_ATTRIBUTE_INVALID.name());
                return;
            } else if(kundeModelVo.getKundenId() <= 0L) {
                log.debug("kundeModelVo.kundenId is negative or zero");
                errors.reject(SeedErrorCode.SEED_ATTRIBUTE_INVALID.name());
                return;
            } else if (!kundeModelVo.getKundenId().equals(kundenId)) {
                log.debug("kundeModelVo.kundenId doesn't match with registered value");
                errors.reject(SeedErrorCode.SEED_ATTRIBUTE_INVALID.name());
                return;
            }
        } catch (KundeException e) {
            log.debug("kundenId is invalid");
            log.error("kundenId is invalid", e);
            errors.reject(SeedErrorCode.SEED_ATTRIBUTE_INVALID.name());
            return;
        }
    }
}
