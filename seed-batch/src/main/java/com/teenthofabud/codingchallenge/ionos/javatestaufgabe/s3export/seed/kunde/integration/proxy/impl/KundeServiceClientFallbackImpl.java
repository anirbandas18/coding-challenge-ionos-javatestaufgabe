package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.data.KundeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy.KundeServiceClient;
import org.springframework.stereotype.Component;

@Component
public class KundeServiceClientFallbackImpl implements KundeServiceClient {
    @Override
    public KundeModelVo getKundeModelDetailsByKundenId(String kundenId) {
        return new KundeModelVo();
    }

    @Override
    public String postNewKundeModel(KundeModelForm form) {
        return "";
    }
}
