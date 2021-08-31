package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.proxy;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.data.KundeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.error.KundeException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.error.KundeServiceClientExceptionHandler;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.proxy.impl.KundeServiceClientFallbackImpl;
import com.teenthofabud.core.common.marker.TOABFeignErrorHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "kunde-service-client", url = "${s3export.sync.kunde-service.url}", fallback = KundeServiceClientFallbackImpl.class)
public interface KundeServiceClient {

    @GetMapping("/model/kundenid/{kundenId}")
    @TOABFeignErrorHandler(KundeServiceClientExceptionHandler.class)
    public KundeModelVo getKundeModelDetailsByKundenId(@PathVariable String kundenId) throws KundeException;

    @PostMapping("/model")
    @TOABFeignErrorHandler(KundeServiceClientExceptionHandler.class)
    public String postNewKundeModel(@RequestBody KundeModelForm form) throws KundeException;

}