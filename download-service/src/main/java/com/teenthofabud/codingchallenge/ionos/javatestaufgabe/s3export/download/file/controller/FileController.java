package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.controller;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.service.AuditService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.service.FileService;
import com.teenthofabud.core.common.constant.TOABBaseAction;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.core.common.service.TOABAuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("file")
@Slf4j
@Tag(name = "File Search API", description = "Get files and their details")
public class FileController implements TOABAuditService {

    private static final Long DEFAULT_USER_ID = 1L;

    private AuditService audit;

    @Autowired
    public void setService(FileService service) {
        this.service = service;
    }

    private FileService service;

    @Autowired
    public void setAudit(AuditService audit) {
        this.audit = audit;
    }

    private AuditForm getAudit(String action, String message, Long userSequence, String input, String output) {
        AuditForm form = new AuditForm();
        form.setAction(action);
        form.setModule(module());
        form.setDescription(message);
        form.setInput(input);
        form.setOutput(output);
        form.setUserSequence(userSequence);
        return form;
    }

    @Operation(summary = "Download latest customer data of a country as CSV file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "CSV file with the latest customer data for the country",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = FileVo.class))) }),
        @ApiResponse(responseCode = "400", description = "File country is invalid",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
        @ApiResponse(responseCode = "404", description = "No customer data available for the given country",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("country/{country}")
    public ResponseEntity<?> getLatestFileByCountry(@PathVariable String country, @Parameter(in = ParameterIn.HEADER, description = "user sequence, a whole number greater than zero")
        @RequestHeader(required = true, name = "S3EXPORT-USER-SEQ") String s3exportUserSeq) throws FileException, AuditException {
        Long userSequence = 0L;
        try {
            userSequence = Long.parseLong(s3exportUserSeq);
        } catch (NumberFormatException e) {
            String msg = "Invalid user sequence";
            log.debug(msg + " : " + s3exportUserSeq, e);
            AuditForm form = getAudit(TOABBaseAction.READ.getName(), msg + ": " + e.getMessage(), DEFAULT_USER_ID, "S3EXPORT-USER-SEQ: " + s3exportUserSeq, "");
            audit.createAudit(form);
            throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "S3EXPORT-USER-SEQ", s3exportUserSeq });
        }
        log.debug("Requesting the latest File content by date with given country");
        if(StringUtils.hasText(StringUtils.trimWhitespace(country))) {
            FileVo matchedByCountry = service.retrieveBy(country);
            ByteArrayResource resource = new ByteArrayResource(matchedByCountry.getContent());
            log.debug("Responding with the file: {}", matchedByCountry.getName());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + matchedByCountry.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, matchedByCountry.getType());
            String msg = "Downloading file";
            AuditForm form = getAudit(TOABBaseAction.READ.getName(), msg, userSequence,  "Country: " + country,
                    "File name: " + matchedByCountry.getName() + ", Type: " + matchedByCountry.getType() + ", Length: " + matchedByCountry.getContent().length);
            audit.createAudit(form);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(matchedByCountry.getContent().length)
                    .body(resource);
        }
        String msg = "File country is empty";
        log.debug(msg);
        AuditForm form = getAudit(TOABBaseAction.READ.getName(), msg, userSequence, "country: " + country, "");
        audit.createAudit(form);
        throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "country", country });
    }

    @Operation(summary = "Download customer data of a country as CSV file by date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV file with customer data for the given the date and country",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = FileVo.class))) }),
            @ApiResponse(responseCode = "400", description = "File country/date is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No customer data available for the given country on specified date",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("country/{country}/date/{date}")
    public ResponseEntity<?> getFileForCountryOndate(@PathVariable String country, @Parameter(in = ParameterIn.HEADER, description = "user sequence, a whole number greater than zero")
    @RequestHeader(required = true, name = "S3EXPORT-USER-SEQ") String s3exportUserSeq, @Parameter(description = "yyyy-MM-dd_HH-mm-ss") @PathVariable String date)
            throws FileException, AuditException {
        Long userSequence = 0L;
        try {
            userSequence = Long.parseLong(s3exportUserSeq);
        } catch (NumberFormatException e) {
            String msg = "Invalid user sequence";
            log.debug(msg + " : " + s3exportUserSeq, e);
            AuditForm form = getAudit(TOABBaseAction.READ.getName(), msg + ": " + e.getMessage(), DEFAULT_USER_ID, "S3EXPORT-USER-SEQ: " + s3exportUserSeq, "");
            audit.createAudit(form);
            throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "S3EXPORT-USER-SEQ", s3exportUserSeq });
        }
        log.debug("Requesting the file content on date for the given country");
        if(StringUtils.hasText(StringUtils.trimWhitespace(country)) && StringUtils.hasText(StringUtils.trimWhitespace(date))) {
            FileVo matchedByCountryAndDate = service.retrieveBy(country, date);
            ByteArrayResource resource = new ByteArrayResource(matchedByCountryAndDate.getContent());
            log.debug("Responding with the file content by date with given country for the specified date");
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + matchedByCountryAndDate.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, matchedByCountryAndDate.getType());
            String msg = "Downloading file";
            AuditForm form = getAudit(TOABBaseAction.READ.getName(), msg, userSequence,  "Country: " + country + ", Date: " + date,
                    "File name: " + matchedByCountryAndDate.getName() + ", Type: " + matchedByCountryAndDate.getType() + ", Length: " + matchedByCountryAndDate.getContent().length);
            audit.createAudit(form);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(matchedByCountryAndDate.getContent().length)
                    .body(resource);
        } else if(country == null || country.length() == 0) {
            String msg = "File country is empty";
            log.debug(msg);
            AuditForm form = getAudit(TOABBaseAction.READ.getName(), msg, userSequence, "country: " + country, "");
            audit.createAudit(form);
            throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "country", country });
        } else  {
            String msg = "File date is empty";
            log.debug(msg);
            AuditForm form = getAudit(TOABBaseAction.READ.getName(), msg, userSequence, "date: " + date, "");
            audit.createAudit(form);
            throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "date", date });
        }
    }

    @Override
    public String module() {
        return "File";
    }

}
