package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.controller;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileMessageTemplate;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.service.FileService;
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
public class FileController {

    @Autowired
    public void setService(FileService service) {
        this.service = service;
    }

    private FileService service;

    @Operation(summary = "Download content of latest file by date of country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Downloaded content of the lastest file by date of the given country",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = FileVo.class))) }),
        @ApiResponse(responseCode = "400", description = "File country is invalid",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
        @ApiResponse(responseCode = "404", description = "No Files available with the given country",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("country/{country}")
    public ResponseEntity<?> getAllFilesByCountry(@PathVariable String country) throws FileException {
        log.debug("Requesting the latest File content by date with given country");
        if(StringUtils.hasText(StringUtils.trimWhitespace(country))) {
            FileVo matchedByCountry = service.retrieveBy(country);
            ByteArrayResource resource = new ByteArrayResource(matchedByCountry.getContent());
            log.debug("Responding with the latest File content by date with given country");
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + matchedByCountry.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, matchedByCountry.getType());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(matchedByCountry.getContent().length)
                    .body(resource);
        }
        log.debug("File country is empty");
        throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "country", country });
    }

    /*@Operation(summary = "Get all File details by date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available Files and their details that match the given date",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = FileVo.class))) }),
            @ApiResponse(responseCode = "400", description = "File date is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No Files available with the given date",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("date/{date}")
    public List<FileVo> getAllFilesByDate(@PathVariable String date) throws FileException {
        log.debug("Requesting all available Files with given date");
        if(StringUtils.hasText(StringUtils.trimWhitespace(date))) {
            List<FileVo> matchedByActions = service.retrieveAllForDate(date);
            log.debug("Responding with all available Files with given date");
            return matchedByActions;
        }
        log.debug("File date is empty");
        throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "date", date });
    }

    @Operation(summary = "Get File details by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve the details of File that matches the given ˇ",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FileVo.class)) }),
            @ApiResponse(responseCode = "400", description = "File name is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No File found with the given name",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("name/{name}")
    public FileVo getauditDetailsById(@PathVariable String name) throws FileException {
        log.debug("Requesting all available Files by its name");
        if(StringUtils.hasText(StringUtils.trimWhitespace(name))) {
            FileVo fileDetails = service.retrieveByName(name);
            log.debug("Responding with successful retrieval of existing File details by name");
            return fileDetails;
        }
        log.debug(FileMessageTemplate.MSG_TEMPLATE_BUCKET_NAME_EMPTY.getValue());
        throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "name", name });
    }*/

}
