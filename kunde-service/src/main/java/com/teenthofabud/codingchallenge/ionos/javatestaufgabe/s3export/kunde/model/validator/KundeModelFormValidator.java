package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.validator;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.error.KundeErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class KundeModelFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(KundeModelForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        KundeModelForm form = (KundeModelForm) target;
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getEmail()))) {
            log.debug("KundeModelForm.email is empty");
            errors.rejectValue("email", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getLand()))) {
            log.debug("KundeModelForm.land is empty");
            errors.rejectValue("land", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getFirmenName()))) {
            log.debug("KundeModelForm.firmenName is empty");
            errors.rejectValue("firmenName", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getNachName()))) {
            log.debug("KundeModelForm.nachName is empty");
            errors.rejectValue("nachName", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getStrasse()))) {
            log.debug("KundeModelForm.strasse is empty");
            errors.rejectValue("strasse", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getOrt()))) {
            log.debug("KundeModelForm.ort is empty");
            errors.rejectValue("ort", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getPlz()))) {
            log.debug("KundeModelForm.plz is empty");
            errors.rejectValue("plz", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getVorName()))) {
            log.debug("KundeModelForm.vorName is empty");
            errors.rejectValue("vorName", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getStrassenZuSatz()))) {
            log.debug("KundeModelForm.strassenZuSatz is empty");
            errors.rejectValue("strassenZuSatz", KundeErrorCode.KUNDE_ATTRIBUTE_INVALID.name());
            return;
        }
    }
}
