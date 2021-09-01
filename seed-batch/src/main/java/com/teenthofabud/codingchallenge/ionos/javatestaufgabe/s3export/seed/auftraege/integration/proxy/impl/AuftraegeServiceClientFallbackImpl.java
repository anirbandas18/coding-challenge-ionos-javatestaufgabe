package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy.AuftraegeServiceClient;
import org.springframework.stereotype.Component;

@Component
public class AuftraegeServiceClientFallbackImpl implements AuftraegeServiceClient {

    @Override
    public AuftraegeModelVo getAuftraegeModelDetailsByAuftragIdArtikelNummerKundenId(String auftragId, String artikelNummer, String kundenId) {
        return new AuftraegeModelVo();
    }

    @Override
    public String postNewAuftraegeModel(AuftraegeModelForm form) {
        return "";
    }
}
