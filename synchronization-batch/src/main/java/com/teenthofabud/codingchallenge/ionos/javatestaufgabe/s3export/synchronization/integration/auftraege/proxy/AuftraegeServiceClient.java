package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.proxy;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.error.AuftraegeException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.error.AuftraegeServiceClientExceptionHandler;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.proxy.impl.AuftraegeServiceClientFallbackImpl;
import com.teenthofabud.core.common.marker.TOABFeignErrorHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "auftraege-service-client", url = "${s3export.sync.auftraege-service.url}", fallback = AuftraegeServiceClientFallbackImpl.class)
public interface AuftraegeServiceClient {

    @GetMapping("/model/range")
    @TOABFeignErrorHandler(AuftraegeServiceClientExceptionHandler.class)
    public List<AuftraegeModelVo> getAuftraegeModelDetailsWithinTheLastNTime(@RequestParam(required = false) String amount,
                                                                             @RequestParam(required = false) String unit) throws AuftraegeException;

    @GetMapping("/model/filter")
    @TOABFeignErrorHandler(AuftraegeServiceClientExceptionHandler.class)
    public AuftraegeModelVo getAuftraegeModelDetailsByAuftragIdArtikelNummerKundenId(
            @RequestParam(required = false) String auftragId, @RequestParam(required = false) String artikelNummer, @RequestParam(required = false) String kundenId)
            throws AuftraegeException;

    @PostMapping("/model")
    @TOABFeignErrorHandler(AuftraegeServiceClientExceptionHandler.class)
    public String postNewAuftraegeModel(@RequestBody AuftraegeModelForm form) throws AuftraegeException;

}