package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.proxy.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.error.AuftraegeException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.proxy.AuftraegeServiceClient;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class AuftraegeServiceClientFallbackImpl implements AuftraegeServiceClient {

    @Override
    public List<AuftraegeModelVo> getAuftraegeModelDetailsWithinTheLastNTime(String amount, String unit) throws AuftraegeException {
        return new LinkedList<>();
    }

    @Override
    public AuftraegeModelVo getAuftraegeModelDetailsByAuftragIdArtikelNummerKundenId(String auftragId, String artikelNummer, String kundenId) throws AuftraegeException {
        return new AuftraegeModelVo();
    }

    @Override
    public String postNewAuftraegeModel(AuftraegeModelForm form) {
        return "";
    }
}
