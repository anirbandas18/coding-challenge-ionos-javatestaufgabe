package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.service.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.error.AuftraegeErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.converter.AuftraegeModelEntity2VoConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.converter.AuftraegeModelForm2EntityConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.*;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.repository.AuftraegeModelRepository;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.service.AuftraegeModelService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.validator.AuftraegeModelFormValidator;
import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Errors;

import java.util.*;

@Component
@Slf4j
public class AuftraegeModelServiceImpl implements AuftraegeModelService {

    private static final Comparator<AuftraegeModelVo> CMP_BY_AUFTRAG_ID_ARTIKEL_NUMMER_AUFTRAEGEN_ID =
            Comparator.comparing(AuftraegeModelVo::getKundenId)
                .thenComparing(AuftraegeModelVo::getAuftragId)
                .thenComparing(AuftraegeModelVo::getArtikelNummer);

    private AuftraegeModelEntity2VoConverter entity2VoConverter;
    private AuftraegeModelForm2EntityConverter form2EntityConverter;
    private AuftraegeModelFormValidator formValidator;
    private AuftraegeModelRepository repository;

    @Autowired
    public void setEntity2VoConverter(AuftraegeModelEntity2VoConverter entity2VoConverter) {
        this.entity2VoConverter = entity2VoConverter;
    }

    @Autowired
    public void setForm2EntityConverter(AuftraegeModelForm2EntityConverter form2EntityConverter) {
        this.form2EntityConverter = form2EntityConverter;
    }

    @Autowired
    public void setRepository(AuftraegeModelRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFormValidator(AuftraegeModelFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    private List<AuftraegeModelVo> entity2DetailedVoList(List<AuftraegeModelEntity> kundeModelEntityList) {
        List<AuftraegeModelVo> kundeModelDetailsList = new ArrayList<>(kundeModelEntityList.size());
        for(AuftraegeModelEntity entity : kundeModelEntityList) {
            AuftraegeModelVo vo = entity2VoConverter.convert(entity);
            log.debug("Converting {} to {}", entity, vo);
            kundeModelDetailsList.add(vo);
        }
        return kundeModelDetailsList;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AuftraegeModelVo> retrieveAllByNaturalOrdering() {
        log.info("Requesting all AuftraegeModelEntity by their natural ordering");
        List<AuftraegeModelEntity> kundeModelEntityList = repository.findAll();
        Set<AuftraegeModelVo> naturallyOrderedSet = new TreeSet<AuftraegeModelVo>(CMP_BY_AUFTRAG_ID_ARTIKEL_NUMMER_AUFTRAEGEN_ID);
        for(AuftraegeModelEntity entity : kundeModelEntityList) {
            AuftraegeModelVo dto = entity2VoConverter.convert(entity);
            log.debug("Converting {} to {}", entity, dto);
            naturallyOrderedSet.add(dto);
        }
        log.info("{} AuftraegeModelVo available", naturallyOrderedSet.size());
        return naturallyOrderedSet;
    }

    @Override
    @Transactional
    public AuftraegeModelVo retrieveDetailsByAuftragIdAndArtikelNummerAndKundenId(String auftragId, String artikelNummer, String kundenId) throws AuftraegeModelException {
        log.info("Requesting AuftraegeModelEntity by auftragId: {}, artikelNummer; {}, kundednId: {}", auftragId, artikelNummer, kundenId);
        Optional<AuftraegeModelEntity> optionalAuftraegeModelEntity = repository.
                findByPrimaryKeyAuftragIdAndPrimaryKeyArtikelNummerAndPrimaryKeyKundenId(auftragId, artikelNummer, kundenId);
        if(optionalAuftraegeModelEntity.isEmpty()) {
            log.debug("No AuftraegeModelEntity found by auftragId: {}, artikelNummer; {}, kundednId: {}", auftragId, artikelNummer, kundenId);
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_NOT_FOUND,
                    new Object[] { "auftragId: " + auftragId + ", artikelNummer: " + artikelNummer, "kundenId: " + kundenId });
        }
        AuftraegeModelEntity entity = optionalAuftraegeModelEntity.get();
        AuftraegeModelVo vo = entity2VoConverter.convert(entity);
        log.info("Found AuftraegeModelVo by auftragId: {}, artikelNummer; {}, kundednId: {}", auftragId, artikelNummer, kundenId);
        return vo;
    }

    @Override
    @Transactional(readOnly = true)
    public AuftraegeModelVo retrieveDetailsByAuftragId(String auftragId) throws AuftraegeModelException {
        log.info("Requesting AuftraegeModelEntity by auftragId: {}", auftragId);
        Optional<AuftraegeModelEntity> optEntity = repository.
                findByPrimaryKeyAuftragId(auftragId);
        if(optEntity.isEmpty()) {
            log.debug("No AuftraegeModelEntity found by auftragId: {}", auftragId);
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_NOT_FOUND, new Object[] { "auftragId", auftragId });
        }
        AuftraegeModelEntity entity = optEntity.get();
        AuftraegeModelVo vo = entity2VoConverter.convert(entity);
        log.info("Found AuftraegeModelVo by auftragId: {}", auftragId);
        return vo;
    }

    @Override
    public List<AuftraegeModelVo> retrieveAllMatchingDetailsByArtikelNummer(String artikelNummer) throws AuftraegeModelException {
        log.info("Requesting AuftraegeModelEntity that match with artikelNummer: {}", artikelNummer);
        List<AuftraegeModelEntity> kundeModelEntityList = repository.
                findByPrimaryKeyArtikelNummerContaining(artikelNummer);
        if(kundeModelEntityList != null && !kundeModelEntityList.isEmpty()) {
            List<AuftraegeModelVo> matchedAuftraegeModelList = entity2DetailedVoList(kundeModelEntityList);
            log.info("Found {} AuftraegeModelVo matching with artikelNummer: {}", matchedAuftraegeModelList.size(),artikelNummer);
            return matchedAuftraegeModelList;
        }
        log.debug("No AuftraegeModelVo found matching with artikelNummer: {}", artikelNummer);
        throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_NOT_FOUND, new Object[] { "artikelNummer", artikelNummer });
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuftraegeModelVo> retrieveAllMatchingDetailsByKundenId(String kundenId) throws AuftraegeModelException {
        log.info("Requesting AuftraegeModelEntity that match with kundenId: {}", kundenId);
        List<AuftraegeModelEntity> kundeModelEntityList = repository.
                findByPrimaryKeyKundenIdContaining(kundenId);
        if(kundeModelEntityList != null && !kundeModelEntityList.isEmpty()) {
            List<AuftraegeModelVo> matchedAuftraegeModelList = entity2DetailedVoList(kundeModelEntityList);
            log.info("Found {} AuftraegeModelVo matching with kundenId: {}", matchedAuftraegeModelList.size(),kundenId);
            return matchedAuftraegeModelList;
        }
        log.debug("No AuftraegeModelVo found matching with kundenId: {}", kundenId);
        throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_NOT_FOUND, new Object[] { "kundenId", kundenId });
    }

    @Override
    public List<AuftraegeModelVo> retrieveAllDetailsWithinLastNTime(Long timeAmount, String timeUnit) throws AuftraegeModelException {
        log.info("Requesting AuftraegeModelEntity within the last {} {}", timeAmount, timeUnit);
        List<AuftraegeModelEntity> auftraegeModelEntities = new LinkedList<>();
        switch(timeUnit) {
            case "hour":
                auftraegeModelEntities = repository.findAllWithinTheLastNHourNowAsPerUTC(timeAmount);
                break;
            case "minute":
                auftraegeModelEntities = repository.findAllWithinTheLastNMinuteNowAsPerUTC(timeAmount);
                break;
            case "second":
                auftraegeModelEntities = repository.findAllWithinTheLastNSecondNowAsPerUTC(timeAmount);
                break;
        }
        if(auftraegeModelEntities != null && !auftraegeModelEntities.isEmpty()) {
            List<AuftraegeModelVo> matchedAuftraegeModelList = entity2DetailedVoList(auftraegeModelEntities);
            log.info("Found {} AuftraegeModelVo within the last {} {}", timeAmount, timeUnit);
            return matchedAuftraegeModelList;
        }
        log.debug("No AuftraegeModelVo found within the last {} {}", timeAmount, timeUnit);
        throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_NOT_FOUND, new Object[] { "timeAmount: " + timeAmount, "timeUnit: " + timeUnit });

    }

    @Override
    @Transactional
    public String createAuftraegeModel(AuftraegeModelForm form) throws AuftraegeModelException {
        log.info("Creating new AuftraegeModelEntity");

        if(form == null) {
            log.debug("AuftraegeModelForm provided is null");
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_UNEXPECTED,
                    new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details: {}", form);

        log.debug("Validating provided attributes of AuftraegeModelForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        formValidator.validate(form, err);
        if(err.hasErrors()) {
            log.debug("AuftraegeModelForm has {} errors", err.getErrorCount());
            AuftraegeErrorCode ec = AuftraegeErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("AuftraegeModelForm error detail: {}", ec);
            throw new AuftraegeModelException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of AuftraegeModelForm are valid");

        log.debug(AuftraegeModelMessageTemplate.MSG_TEMPLATE_MODEL_EXISTENCE_BY_ID_ARTIKEL_NUMMER_KUNDE_ID.getValue(),
                form.getAuftragId(), form.getArtikelNummer(), form.getKundenId());
        AuftraegeModelEntity expectedEntity = form2EntityConverter.convert(form);
        if(repository.existsByPrimaryKeyAuftragIdAndPrimaryKeyArtikelNummerAndPrimaryKeyKundenId(expectedEntity.getPrimaryKey().getAuftragId(),
                expectedEntity.getPrimaryKey().getArtikelNummer(), expectedEntity.getPrimaryKey().getKundenId())) {
            log.debug(AuftraegeModelMessageTemplate.MSG_TEMPLATE_MODEL_EXISTS_BY_ID_ARTIKEL_NUMMER_KUNDE_ID.getValue(),
                    expectedEntity.getPrimaryKey().getAuftragId(), expectedEntity.getPrimaryKey().getArtikelNummer(), expectedEntity.getPrimaryKey().getKundenId());
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_EXISTS,
                    new Object[]{ "auftragId: " + form.getAuftragId() + ", artikelNummer: " + form.getArtikelNummer(), "kundeId: " + form.getKundenId() });
        }
        log.debug(AuftraegeModelMessageTemplate.MSG_TEMPLATE_MODEL_NON_EXISTENCE_BY_ID_ARTIKEL_NUMMER_KUNDE_ID.getValue(),
                expectedEntity.getPrimaryKey().getAuftragId(), expectedEntity.getPrimaryKey().getArtikelNummer(), expectedEntity.getPrimaryKey().getKundenId());

        log.debug("Saving {}", expectedEntity);
        AuftraegeModelEntity actualEntity = repository.save(expectedEntity);
        log.debug("Saved {}", actualEntity);

        if(actualEntity == null) {
            log.debug("Unable to create {}", expectedEntity);
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ACTION_FAILURE,
                    new Object[]{ "creation", "unable to persist AuftraegeModelForm details" });
        }
        log.info("Created new AuftraegeModelForm with auftragId: {}, artikelNummer: {}, kundeId: {}",
                actualEntity.getPrimaryKey().getAuftragId(), actualEntity.getPrimaryKey().getArtikelNummer(), actualEntity.getPrimaryKey().getKundenId());
        return actualEntity.getPrimaryKey().getAuftragId();
    }

}