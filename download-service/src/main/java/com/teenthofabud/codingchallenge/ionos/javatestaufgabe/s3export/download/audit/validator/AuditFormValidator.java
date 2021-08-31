package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.validator;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class AuditFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AuditForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AuditForm form = (AuditForm) target;
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getModule()))) {
            log.debug("AuditForm.module is empty");
            errors.rejectValue("module", DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getAction()))) {
            log.debug("AuditForm.action is empty");
            errors.rejectValue("action", DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID.name());
            return;
        }
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(form.getDescription()))) {
            log.debug("AuditForm.description is empty");
            errors.rejectValue("description", DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID.name());
            return;
        }
        if(form.getUserSequence() == null || form.getUserSequence() <= 0L) {
            log.debug("AuditForm.userSequence is empty");
            errors.rejectValue("userSequence", DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID.name());
            return;
        }
    }
}
