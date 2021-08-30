package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.converter.AuditEntity2VoConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.converter.AuditForm2EntityConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.*;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.repository.AuditRepository;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.service.AuditService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.validator.AuditFormValidator;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.core.common.service.TOABBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Errors;

import java.util.*;

@Component
@Slf4j
public class AuditServiceImpl implements AuditService {

    private static final Comparator<AuditVo> CMP_BY_MODULE = (s1, s2) -> {
        return s1.getModule().compareTo(s2.getModule());
    };

    private AuditEntity2VoConverter entity2VoConverter;
    private AuditForm2EntityConverter form2EntityConverter;
    private AuditFormValidator formValidator;
    private AuditRepository repository;
    private TOABBaseService toabBaseService;
    private ObjectMapper om;

    @Autowired
    public void setEntity2VoConverter(AuditEntity2VoConverter entity2VoConverter) {
        this.entity2VoConverter = entity2VoConverter;
    }

    @Autowired
    public void setPatchOperationValidator(TOABBaseService toabBaseService) {
        this.toabBaseService = toabBaseService;
    }

    @Autowired
    public void setOm(ObjectMapper om) {
        this.om = om;
    }

    @Autowired
    public void setForm2EntityConverter(AuditForm2EntityConverter form2EntityConverter) {
        this.form2EntityConverter = form2EntityConverter;
    }

    @Autowired
    public void setRepository(AuditRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFormValidator(AuditFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    private List<AuditVo> entity2DetailedVoList(List<AuditEntity> auditEntityList) {
        List<AuditVo> AuditDetailsList = new ArrayList<>(auditEntityList.size());
        for(AuditEntity entity : auditEntityList) {
            AuditVo vo = entity2VoConverter.convert(entity);
            log.debug("Converting {} to {}", entity, vo);
            AuditDetailsList.add(vo);
        }
        return AuditDetailsList;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AuditVo> retrieveAllByNaturalOrdering() {
        log.info("Requesting all AuditEntity by their natural ordering");
        List<AuditEntity> auditEntityList = repository.findAll();
        Set<AuditVo> naturallyOrderedSet = new TreeSet<AuditVo>(CMP_BY_MODULE);
        for(AuditEntity entity : auditEntityList) {
            AuditVo dto = entity2VoConverter.convert(entity);
            log.debug("Converting {} to {}", entity, dto);
            naturallyOrderedSet.add(dto);
        }
        log.info("{} AuditVo available", naturallyOrderedSet.size());
        return naturallyOrderedSet;
    }

    @Override
    @Transactional(readOnly = true)
    public AuditVo retrieveDetailsById(Long id) throws AuditException {
        log.info("Requesting AuditEntity by id: {}", id);
        Optional<AuditEntity> optEntity = repository.findById(id);
        if(optEntity.isEmpty()) {
            log.debug("No AuditEntity found by id: {}", id);
            throw new AuditException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        AuditEntity entity = optEntity.get();
        AuditVo vo = entity2VoConverter.convert(entity);
        log.info("Found AuditVo by id: {}", id);
        return vo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditVo> retrieveAllMatchingDetailsByAction(String action) throws AuditException {
        log.info("Requesting AuditEntity that match with action: {}", action);
        List<AuditEntity> auditEntityList = repository.findByActionContaining(action);
        if(auditEntityList != null && !auditEntityList.isEmpty()) {
            List<AuditVo> matchedAuditList = entity2DetailedVoList(auditEntityList);
            log.info("Found {} AuditVo matching with action: {}", matchedAuditList.size(),action);
            return matchedAuditList;
        }
        log.debug("No AuditVo found matching with action: {}", action);
        throw new AuditException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "action", action });
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditVo> retrieveAllMatchingDetailsByModule(String module) throws AuditException {
        log.info("Requesting AuditEntity that match with module: {}", module);
        List<AuditEntity> auditEntityList = repository.findByModuleContaining(module);
        if(auditEntityList != null && !auditEntityList.isEmpty()) {
            List<AuditVo> matchedAuditList = entity2DetailedVoList(auditEntityList);
            log.info("Found {} AuditVo matching with module: {}", matchedAuditList.size(),module);
            return matchedAuditList;
        }
        log.debug("No AuditVo found matching with module: {}", module);
        throw new AuditException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "module", module });
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditVo> retrieveAllMatchingDetailsByModuleAndAction(String module, String action) throws AuditException {
        log.info("Requesting AuditEntity that match with module: {} and action: {}", module, action);
        List<AuditEntity> auditEntityList = repository.findByModuleAndAction(module, action);
        if(auditEntityList != null && !auditEntityList.isEmpty()) {
            List<AuditVo> matchedAuditList = entity2DetailedVoList(auditEntityList);
            log.info("Found {} AuditVo matching with module: {} and action: {}", matchedAuditList.size(), module, action);
            return matchedAuditList;
        }
        log.debug("No AuditVo found matching with module: {} and action: {}", module, action);
        throw new AuditException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "module: " + module, "action: " + action });
    }

    @Override
    @Transactional
    public Long createAudit(AuditForm form) throws AuditException {
        log.info("Creating new AuditEntity");

        if(form == null) {
            log.debug("AuditForm provided is null");
            throw new AuditException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_UNEXPECTED,
                    new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details: {}", form);

        log.debug("Validating provided attributes of AuditForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        formValidator.validate(form, err);
        if(err.hasErrors()) {
            log.debug("AuditForm has {} errors", err.getErrorCount());
            DownloadErrorCode ec = DownloadErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("AuditForm error detail: {}", ec);
            throw new AuditException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of AuditForm are valid");

        AuditEntity expectedEntity = form2EntityConverter.convert(form);

        log.debug("Saving {}", expectedEntity);
        AuditEntity actualEntity = repository.save(expectedEntity);
        log.debug("Saved {}", actualEntity);

        if(actualEntity == null) {
            log.debug("Unable to create {}", expectedEntity);
            throw new AuditException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE,
                    new Object[]{ "creation", "unable to persist AuditForm details" });
        }
        log.info("Created new AuditForm with id: {}", actualEntity.getId());
        return actualEntity.getId();
    }

}