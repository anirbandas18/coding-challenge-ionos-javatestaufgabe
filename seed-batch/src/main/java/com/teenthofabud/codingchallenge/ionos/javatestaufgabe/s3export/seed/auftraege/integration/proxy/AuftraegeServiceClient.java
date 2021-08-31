package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.error.AuftraegeException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.error.AuftraegeServiceClientExceptionHandler;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy.impl.AuftraegeServiceClientFallbackImpl;
import com.teenthofabud.core.common.marker.TOABFeignErrorHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "auftraege-service-client", url = "${s3export.seed.auftraege-service.url}", fallback = AuftraegeServiceClientFallbackImpl.class)
public interface AuftraegeServiceClient {

    @GetMapping("/model/filter")
    @TOABFeignErrorHandler(AuftraegeServiceClientExceptionHandler.class)
    public AuftraegeModelVo getAuftraegeModelDetailsByAuftragIdArtikelNummerKundenId(
            @RequestParam(required = false) String auftragId, @RequestParam(required = false) String artikelNummer, @RequestParam(required = false) String kundenId)
            throws AuftraegeException;

    @PostMapping("/model")
    @TOABFeignErrorHandler(AuftraegeServiceClientExceptionHandler.class)
    public String postNewAuftraegeModel(@RequestBody AuftraegeModelForm form) throws AuftraegeException;

}