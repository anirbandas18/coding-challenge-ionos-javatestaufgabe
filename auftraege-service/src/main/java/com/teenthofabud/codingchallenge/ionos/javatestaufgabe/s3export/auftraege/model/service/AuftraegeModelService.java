package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface AuftraegeModelService {

    public Set<AuftraegeModelVo> retrieveAllByNaturalOrdering();

    public AuftraegeModelVo retrieveDetailsByAuftragIdAndArtikelNummerAndKundenId(String auftragId, String artikelNummer, String kundenId) throws AuftraegeModelException;

    public AuftraegeModelVo retrieveDetailsByAuftragId(String auftragId) throws AuftraegeModelException;

    public List<AuftraegeModelVo> retrieveAllMatchingDetailsByArtikelNummer(String artikelNummer) throws AuftraegeModelException;

    public List<AuftraegeModelVo> retrieveAllMatchingDetailsByKundenId(String kundenId) throws AuftraegeModelException;

    public List<AuftraegeModelVo> retrieveAllDetailsWithinLastNTime(Long timeAmount, String timeUnit) throws AuftraegeModelException;

    public String createAuftraegeModel(AuftraegeModelForm form) throws AuftraegeModelException;

}
