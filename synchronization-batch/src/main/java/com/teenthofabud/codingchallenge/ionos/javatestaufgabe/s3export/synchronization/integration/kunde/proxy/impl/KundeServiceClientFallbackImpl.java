package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.proxy.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.data.KundeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.proxy.KundeServiceClient;
import org.springframework.stereotype.Component;

@Component
public class KundeServiceClientFallbackImpl implements KundeServiceClient {
    @Override
    public KundeModelVo getKundeModelDetailsByKundenId(String kundenId){
        return new KundeModelVo();
    }

    @Override
    public String postNewKundeModel(KundeModelForm form) {
        return "";
    }
}
