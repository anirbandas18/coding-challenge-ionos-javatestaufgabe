package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.controller;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.error.KundeErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelMessageTemplate;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.service.KundeModelService;
import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Kunde API", description = "Manage kunde models and their details")
public class KundeModelController {

    @Autowired
    public void setService(KundeModelService service) {
        this.service = service;
    }

    private KundeModelService service;

    @Operation(summary = "Create new kunde model details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Id of newly created kunde model",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "kunde model attribute's value is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "409", description = "kunde model already exists with the given attribute values",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "422", description = "No kunde model attributes provided",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal system error while trying to create new kunde model",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> postNewKundeModel(@RequestBody(required = false) KundeModelForm form) throws KundeModelException {
        log.debug("Requesting to create new kunde model");
        if(form != null) {
            Long id = service.createKundeModel(form);
            log.debug("Responding with identifier of newly created new kunde model");
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        }
        log.debug("KundeModelForm is null");
        throw new KundeModelException(KundeErrorCode.KUNDE_ATTRIBUTE_UNEXPECTED,
                new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
    }

    @Operation(summary = "Get all kunde model details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available kunde models and their details",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = KundeModelVo.class))) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Set<KundeModelVo> getAllKundeModelNaturallyOrdered() {
        log.debug("Requesting all available kunde models by their natural orders");
        Set<KundeModelVo> naturallyOrderedKundeModels = service.retrieveAllByNaturalOrdering();
        log.debug("Responding with all available kunde models by their natural orders");
        return naturallyOrderedKundeModels;
    }

    @Operation(summary = "Get all kunde model details by email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrieve all available kunde models and their details that match the given email",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = KundeModelVo.class))) }),
        @ApiResponse(responseCode = "400", description = "kunde model email is invalid",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
        @ApiResponse(responseCode = "404", description = "No kunde model available with the given email",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("email/{email}")
    public List<KundeModelVo> getAllKundeModelsByEmail(@PathVariable String email) throws KundeModelException {
        log.debug("Requesting all available kunde models with given email");
        if(StringUtils.hasText(StringUtils.trimWhitespace(email))) {
            List<KundeModelVo> matchedByEmail = service.retrieveAllMatchingDetailsByEmail(email);
            log.debug("Responding with all available kunde models with given email");
            return matchedByEmail;
        }
        log.debug("kunde model email is empty");
        throw new KundeModelException(KundeErrorCode.KUNDE_ATTRIBUTE_INVALID, new Object[] { "email", email });
    }

    @Operation(summary = "Get kunde model details by kunden id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve the details of kunde model that matches the given kunden id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = KundeModelVo.class)) }),
            @ApiResponse(responseCode = "400", description = "kunde model kunden id is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No kunde model found with the given kunden id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("kundenid/{kundenId}")
    public KundeModelVo getKundeModelDetailsByKundenId(@PathVariable String kundenId) throws KundeModelException {
        log.debug("Requesting all available kunde model by its kundenId");
        if(StringUtils.hasText(StringUtils.trimWhitespace(kundenId))) {
            try {
                Long actualId = Long.parseLong(kundenId);
                log.debug(KundeModelMessageTemplate.MSG_TEMPLATE_MODEL_ID_VALID.getValue(), kundenId);
                KundeModelVo kundeModelDetails = service.retrieveDetailsById(actualId);
                log.debug("Responding with successful retrieval of existing kunde model details by kundenId");
                return kundeModelDetails;
            } catch (NumberFormatException e) {
                log.debug(KundeModelMessageTemplate.MSG_TEMPLATE_MODEL_ID_INVALID.getValue(), kundenId);
                throw new KundeModelException(KundeErrorCode.KUNDE_ATTRIBUTE_INVALID, new Object[] { "kundenId", kundenId });
            }
        }
        log.debug(KundeModelMessageTemplate.MSG_TEMPLATE_MODEL_ID_EMPTY.getValue());
        throw new KundeModelException(KundeErrorCode.KUNDE_ATTRIBUTE_INVALID, new Object[] { "kundenId", kundenId });
    }

}
