package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.controller;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.error.AuftraegeErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelMessageTemplate;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.service.AuftraegeModelService;
import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("model")
@Slf4j
@Tag(name = "Auftraege API", description = "Manage auftraege models and their details")
public class AuftraegeModelController {

    @Autowired
    public void setService(AuftraegeModelService service) {
        this.service = service;
    }

    private AuftraegeModelService service;

    @Operation(summary = "Create new auftraege model details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Id of newly created auftraege model",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "auftraege model attribute's value is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "409", description = "auftraege model already exists with the given attribute values",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "422", description = "No auftraege model attributes provided",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal system error while trying to create new auftraege model",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postNewAuftraegeModel(@RequestBody(required = false) AuftraegeModelForm form) throws AuftraegeModelException {
        log.debug("Requesting to create new auftraege model");
        if(form != null) {
            String auftragId = service.createAuftraegeModel(form);
            log.debug("Responding with identifier of newly created new auftraege model");
            return ResponseEntity.status(HttpStatus.CREATED).body(auftragId);
        }
        log.debug("AuftraegeModelForm is null");
        throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_UNEXPECTED,
                new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
    }

    @Operation(summary = "Get all auftraege model details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available auftraege models and their details",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AuftraegeModelVo.class))) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Set<AuftraegeModelVo> getAllAuftraegeModelNaturallyOrdered() {
        log.debug("Requesting all available auftraege models by their natural orders");
        Set<AuftraegeModelVo> naturallyOrderedAuftraegeModels = service.retrieveAllByNaturalOrdering();
        log.debug("Responding with all available auftraege models by their natural orders");
        return naturallyOrderedAuftraegeModels;
    }

    @Operation(summary = "Get all auftraege model details by artikel nummer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrieve all available auftraege models and their details that match the given artikel nummer",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AuftraegeModelVo.class))) }),
        @ApiResponse(responseCode = "400", description = "auftraege model artikel nummer is invalid",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
        @ApiResponse(responseCode = "404", description = "No auftraege model available with the given artikel nummer",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("artikelnummer/{artikelNummer}")
    public List<AuftraegeModelVo> getAllAuftraegeModelsByArtikelNummer(@PathVariable String artikelNummer) throws AuftraegeModelException {
        log.debug("Requesting all available auftraege models with given artikelNummer");
        if(StringUtils.hasText(StringUtils.trimWhitespace(artikelNummer))) {
            List<AuftraegeModelVo> matchedByArtikelNummer = service.retrieveAllMatchingDetailsByArtikelNummer(artikelNummer);
            log.debug("Responding with all available auftraege models with given artikelNummer");
            return matchedByArtikelNummer;
        }
        log.debug("auftraege model artikelNummer is empty");
        throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "artikelNummer", artikelNummer });
    }

    @Operation(summary = "Get all auftraege model details by kunden id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available auftraege models and their details that match the given kunden id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AuftraegeModelVo.class))) }),
            @ApiResponse(responseCode = "400", description = "auftraege model kunden id is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No auftraege model available with the given kunden id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("kundenid/{kundenId}")
    public List<AuftraegeModelVo> getAllAuftraegeModelsByKundenId(@PathVariable String kundenId) throws AuftraegeModelException {
        log.debug("Requesting all available auftraege models with given kundenId");
        if(StringUtils.hasText(StringUtils.trimWhitespace(kundenId))) {
            List<AuftraegeModelVo> matchedByArtikelNummer = service.retrieveAllMatchingDetailsByKundenId(kundenId);
            log.debug("Responding with all available auftraege models with given kundenId");
            return matchedByArtikelNummer;
        }
        log.debug("auftraege model kundenId is empty");
        throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "kundenId", kundenId });
    }

    @Operation(summary = "Get auftraege model details by auftrag id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve the details of auftraege model that matches the given auftrag id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuftraegeModelVo.class)) }),
            @ApiResponse(responseCode = "400", description = "auftraege model auftrag id is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No auftraege model found with the given auftrag id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("auftragid/{auftragId}")
    public AuftraegeModelVo getAuftraegeModelDetailsByAuftragId(@PathVariable String auftragId) throws AuftraegeModelException {
        log.debug("Requesting all available auftraege model by its auftragId");
        if(StringUtils.hasText(StringUtils.trimWhitespace(auftragId))) {
            log.debug(AuftraegeModelMessageTemplate.MSG_TEMPLATE_MODEL_ID_VALID.getValue(), auftragId);
            AuftraegeModelVo auftraegeModelDetails = service.retrieveDetailsByAuftragId(auftragId);
            log.debug("Responding with successful retrieval of existing auftraege model details by auftragId");
            return auftraegeModelDetails;
        }
        log.debug(AuftraegeModelMessageTemplate.MSG_TEMPLATE_MODEL_ID_EMPTY.getValue());
        throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "auftragId", auftragId });
    }

    @Operation(summary = "Get auftraege model details by auftrag id, artikel nummer, kunden id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve the details of auftraege model that matches the given auftrag id, artikel nummer, kunden id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuftraegeModelVo.class)) }),
            @ApiResponse(responseCode = "400", description = "auftraege model auftrag id, artikel nummer, kunden id is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No auftraege model found with the given auftrag id, artikel nummer, kunden id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("filter")
    public AuftraegeModelVo getAuftraegeModelDetailsByAuftragIdArtikelNummerKundenId(@RequestParam(required = false) String auftragId,
        @RequestParam(required = false) String artikelNummer, @RequestParam(required = false) String kundenId) throws AuftraegeModelException {
        log.debug("Requesting all available auftraege model by its auftragId");
        if(StringUtils.hasText(StringUtils.trimWhitespace(auftragId)) && StringUtils.hasText(StringUtils.trimWhitespace(artikelNummer))
                && StringUtils.hasText(StringUtils.trimWhitespace(kundenId))) {
            log.debug("AuftragId {} artikelNummer {} kundenId {} are semantically valid", auftragId, artikelNummer, kundenId);
            AuftraegeModelVo auftraegeModelDetails = service
                    .retrieveDetailsByAuftragIdAndArtikelNummerAndKundenId(auftragId, artikelNummer, kundenId);
            log.debug("Responding with successful retrieval of existing auftraege model details by auftrag id {}, artikel nummer {}, kunden id {}",
                    auftragId, artikelNummer, kundenId);
            return auftraegeModelDetails;
        } else if(StringUtils.hasText(StringUtils.trimWhitespace(auftragId))) {
            log.debug(AuftraegeModelMessageTemplate.MSG_TEMPLATE_MODEL_ID_EMPTY.getValue());
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "auftragId", auftragId });
        } else if(StringUtils.hasText(StringUtils.trimWhitespace(artikelNummer))) {
            log.debug(AuftraegeModelMessageTemplate.MSG_TEMPLATE_MODEL_ARTIKEL_NUMMER_EMPTY.getValue());
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "artikelNummer", artikelNummer });
        } else {
            log.debug(AuftraegeModelMessageTemplate.MSG_TEMPLATE_MODEL_KUNDEN_ID_EMPTY.getValue());
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "kundenId", kundenId });
        }
    }

    @Operation(summary = "Get auftraege model details within the last N time from now")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve the details of auftraege model that lie within the last N time from now",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AuftraegeModelVo.class))) }),
            @ApiResponse(responseCode = "400", description = "timeAmount/timeUnit is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No auftraege model found within that lie within the last N time from now",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("range")
    public List<AuftraegeModelVo> getAuftraegeModelDetailsWithinTheLastNTime(
            @Parameter(required = true, description = "amount of time as a positive whole number") @RequestParam(required = false) String amount,
            @Parameter(required = true, description = "unit of the associated time amount viz., second, minute, hour") @RequestParam(required = false) String unit) throws AuftraegeModelException {
        if(StringUtils.hasText(StringUtils.trimWhitespace(amount)) && StringUtils.hasText(StringUtils.trimWhitespace(unit))) {
            Long timeAmount = -1l;
            try {
                timeAmount = Long.parseLong(amount);
            } catch (NumberFormatException e) {
                log.debug("time amount {} is invalid", amount);
                throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "amount", amount });
            }
            log.debug("Time amount {} and time unit {} are valid", timeAmount, unit);
            log.debug("Requesting all available auftraege model within the last {} {}", timeAmount, unit);
            List<AuftraegeModelVo> auftraegeModelDetailList = service.retrieveAllDetailsWithinLastNTime(timeAmount, unit);
            log.debug("Responding with successful retrieval of existing auftraege model details within the last {} {}", timeAmount, unit);
            return auftraegeModelDetailList;
        } else if(StringUtils.hasText(StringUtils.trimWhitespace(amount))) {
            log.debug("time amount is empty");
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "amount", amount });
        } else {
            log.debug("time unit is empty");
            throw new AuftraegeModelException(AuftraegeErrorCode.AUFTRAEGE_ATTRIBUTE_INVALID, new Object[] { "unit", unit });
        }
    }

}
