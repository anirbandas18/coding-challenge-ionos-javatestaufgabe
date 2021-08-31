package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.error.KundeErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.converter.KundeModelEntity2VoConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.converter.KundeModelForm2EntityConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.*;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.repository.KundeModelRepository;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.service.KundeModelService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.validator.KundeModelFormValidator;
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
public class KundeModelServiceImpl implements KundeModelService {

    private static final Comparator<KundeModelVo> CMP_BY_EMAIL = (s1, s2) -> {
        return s1.getEmail().compareTo(s2.getEmail());
    };

    private KundeModelEntity2VoConverter entity2VoConverter;
    private KundeModelForm2EntityConverter form2EntityConverter;
    private KundeModelFormValidator formValidator;
    private KundeModelRepository repository;

    @Autowired
    public void setEntity2VoConverter(KundeModelEntity2VoConverter entity2VoConverter) {
        this.entity2VoConverter = entity2VoConverter;
    }

    @Autowired
    public void setForm2EntityConverter(KundeModelForm2EntityConverter form2EntityConverter) {
        this.form2EntityConverter = form2EntityConverter;
    }

    @Autowired
    public void setRepository(KundeModelRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFormValidator(KundeModelFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    private List<KundeModelVo> entity2DetailedVoList(List<KundeModelEntity> kundeModelEntityList) {
        List<KundeModelVo> kundeModelDetailsList = new ArrayList<>(kundeModelEntityList.size());
        for(KundeModelEntity entity : kundeModelEntityList) {
            KundeModelVo vo = entity2VoConverter.convert(entity);
            log.debug("Converting {} to {}", entity, vo);
            kundeModelDetailsList.add(vo);
        }
        return kundeModelDetailsList;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<KundeModelVo> retrieveAllByNaturalOrdering() {
        log.info("Requesting all KundeModelEntity by their natural ordering");
        List<KundeModelEntity> kundeModelEntityList = repository.findAll();
        Set<KundeModelVo> naturallyOrderedSet = new TreeSet<KundeModelVo>(CMP_BY_EMAIL);
        for(KundeModelEntity entity : kundeModelEntityList) {
            KundeModelVo dto = entity2VoConverter.convert(entity);
            log.debug("Converting {} to {}", entity, dto);
            naturallyOrderedSet.add(dto);
        }
        log.info("{} KundeModelVo available", naturallyOrderedSet.size());
        return naturallyOrderedSet;
    }

    @Override
    @Transactional(readOnly = true)
    public KundeModelVo retrieveDetailsById(Long id) throws KundeModelException {
        log.info("Requesting KundeModelEntity by id: {}", id);
        Optional<KundeModelEntity> optEntity = repository.findByKundenId(id);
        if(optEntity.isEmpty()) {
            log.debug("No KundeModelEntity found by id: {}", id);
            throw new KundeModelException(KundeErrorCode.KUNDE_NOT_FOUND, new Object[] { "kundeid", String.valueOf(id) });
        }
        KundeModelEntity entity = optEntity.get();
        KundeModelVo vo = entity2VoConverter.convert(entity);
        log.info("Found KundeModelVo by id: {}", id);
        return vo;
    }


    @Override
    @Transactional(readOnly = true)
    public List<KundeModelVo> retrieveAllMatchingDetailsByEmail(String email) throws KundeModelException {
        log.info("Requesting KundeModelEntity that match with name: {}", email);
        List<KundeModelEntity> kundeModelEntityList = repository.findByEmailContaining(email);
        if(kundeModelEntityList != null && !kundeModelEntityList.isEmpty()) {
            List<KundeModelVo> matchedKundeModelList = entity2DetailedVoList(kundeModelEntityList);
            log.info("Found {} KundeModelVo matching with email: {}", matchedKundeModelList.size(),email);
            return matchedKundeModelList;
        }
        log.debug("No KundeModelVo found matching with email: {}", email);
        throw new KundeModelException(KundeErrorCode.KUNDE_NOT_FOUND, new Object[] { "email", email });
    }

    @Override
    @Transactional
    public Long createKundeModel(KundeModelForm form) throws KundeModelException {
        log.info("Creating new KundeModelEntity");

        if(form == null) {
            log.debug("KundeModelForm provided is null");
            throw new KundeModelException(KundeErrorCode.KUNDE_ATTRIBUTE_UNEXPECTED,
                    new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details: {}", form);

        log.debug("Validating provided attributes of KundeModelForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        formValidator.validate(form, err);
        if(err.hasErrors()) {
            log.debug("KundeModelForm has {} errors", err.getErrorCount());
            KundeErrorCode ec = KundeErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("KundeModelForm error detail: {}", ec);
            throw new KundeModelException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of KundeModelForm are valid");

        log.debug(KundeModelMessageTemplate.MSG_TEMPLATE_MODEL_EXISTENCE_BY_EMAIL.getValue(), form.getEmail());
        KundeModelEntity expectedEntity = form2EntityConverter.convert(form);
        if(repository.existsByEmail(expectedEntity.getEmail())) {
            log.debug(KundeModelMessageTemplate.MSG_TEMPLATE_MODEL_EXISTS_BY_EMAIL.getValue(), expectedEntity.getEmail());
            throw new KundeModelException(KundeErrorCode.KUNDE_EXISTS,
                    new Object[]{ "email", form.getEmail() });
        }
        log.debug(KundeModelMessageTemplate.MSG_TEMPLATE_MODEL_NON_EXISTENCE_BY_EMAIL.getValue(), expectedEntity.getEmail());

        log.debug("Saving {}", expectedEntity);
        KundeModelEntity actualEntity = repository.save(expectedEntity);
        log.debug("Saved {}", actualEntity);

        if(actualEntity == null) {
            log.debug("Unable to create {}", expectedEntity);
            throw new KundeModelException(KundeErrorCode.KUNDE_ACTION_FAILURE,
                    new Object[]{ "creation", "unable to persist KundeModelForm details" });
        }
        log.info("Created new KundeModelForm with id: {}", actualEntity.getKundenId());
        return actualEntity.getKundenId();
    }

}