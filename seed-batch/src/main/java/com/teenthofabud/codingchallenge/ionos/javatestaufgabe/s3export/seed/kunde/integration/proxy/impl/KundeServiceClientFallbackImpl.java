package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.data.KundeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.error.KundeException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy.KundeServiceClient;
import org.springframework.stereotype.Component;

@Component
public class KundeServiceClientFallbackImpl implements KundeServiceClient {
    @Override
    public KundeModelVo getKundeModelDetailsByKundenId(String kundenId) throws KundeException {
        return new KundeModelVo();
    }

    @Override
    public String postNewKundeModel(KundeModelForm form) throws KundeException {
        return "";
    }
}
