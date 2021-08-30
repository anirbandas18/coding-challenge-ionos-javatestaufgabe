package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.controller;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditMessageTemplate;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.service.AuditService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("audit")
@Slf4j
@Tag(name = "Audit Search API", description = "Get audits and their details")
public class AuditController {

    @Autowired
    public void setService(AuditService service) {
        this.service = service;
    }

    private AuditService service;
    
    @Operation(summary = "Get all Audit details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available Audits and their details",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AuditVo.class))) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Set<AuditVo> getAllAuditNaturallyOrdered() {
        log.debug("Requesting all available Audits by their natural orders");
        Set<AuditVo> naturallyOrderedAudits = service.retrieveAllByNaturalOrdering();
        log.debug("Responding with all available Audits by their natural orders");
        return naturallyOrderedAudits;
    }

    @Operation(summary = "Get all Audit details by module")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrieve all available Audits and their details that match the given module",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AuditVo.class))) }),
        @ApiResponse(responseCode = "400", description = "Audit module is invalid",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
        @ApiResponse(responseCode = "404", description = "No Audits available with the given module",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("module/{module}")
    public List<AuditVo> getAllAuditsByModule(@PathVariable String module) throws AuditException {
        log.debug("Requesting all available Audits with given module");
        if(StringUtils.hasText(StringUtils.trimWhitespace(module))) {
            List<AuditVo> matchedByModules = service.retrieveAllMatchingDetailsByModule(module);
            log.debug("Responding with all available Audits with given module");
            return matchedByModules;
        }
        log.debug("Audit module is empty");
        throw new AuditException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "module", module });
    }

    @Operation(summary = "Get all Audit details by action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available Audits and their details that match the given action",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AuditVo.class))) }),
            @ApiResponse(responseCode = "400", description = "Audit action is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No Audits available with the given action",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("action/{action}")
    public List<AuditVo> getAllAuditsByAction(@PathVariable String action) throws AuditException {
        log.debug("Requesting all available Audits with given action");
        if(StringUtils.hasText(StringUtils.trimWhitespace(action))) {
            List<AuditVo> matchedByActions = service.retrieveAllMatchingDetailsByAction(action);
            log.debug("Responding with all available Audits with given action");
            return matchedByActions;
        }
        log.debug("Audit action is empty");
        throw new AuditException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "action", action });
    }

    @Operation(summary = "Get Audit details by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve the details of Audit that matches the given id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuditVo.class)) }),
            @ApiResponse(responseCode = "400", description = "Audit id is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No Audit found with the given id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public AuditVo getAuditDetailsById(@PathVariable String id) throws AuditException {
        log.debug("Requesting all available Audits by its id");
        if(StringUtils.hasText(StringUtils.trimWhitespace(id))) {
            try {
                Long actualId = Long.parseLong(id);
                log.debug(AuditMessageTemplate.MSG_TEMPLATE_AUDIT_ID_VALID.getValue(), id);
                AuditVo auditDetails = service.retrieveDetailsById(actualId);
                log.debug("Responding with successful retrieval of existing Audit details by id");
                return auditDetails;
            } catch (NumberFormatException e) {
                log.debug(AuditMessageTemplate.MSG_TEMPLATE_AUDIT_ID_INVALID.getValue(), id);
                throw new AuditException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "id", id });
            }
        }
        log.debug(AuditMessageTemplate.MSG_TEMPLATE_AUDIT_ID_EMPTY.getValue());
        throw new AuditException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "id", id });
    }

}
