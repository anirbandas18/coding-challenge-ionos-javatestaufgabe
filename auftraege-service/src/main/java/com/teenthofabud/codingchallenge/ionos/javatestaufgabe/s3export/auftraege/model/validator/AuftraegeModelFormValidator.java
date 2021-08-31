package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.validator;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.error.AuftraegeErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class AuftraegeModelFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AuftraegeModelForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AuftraegeModelForm form = (AuftraegeModelForm) target;
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getAuftragId()))) {
            log.debug("AuftraegeModelForm.auftraegeId is empty");
            errors.rejectValue("auftraegeId", AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getArtikelNummer()))) {
            log.debug("AuftraegeModelForm.artikelNummer is empty");
            errors.rejectValue("artikelNummer", AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getKundenId()))) {
            log.debug("AuftraegeModelForm.kundenId is empty");
            errors.rejectValue("kundenId", AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID.name());
            return;
        }
    }
}
