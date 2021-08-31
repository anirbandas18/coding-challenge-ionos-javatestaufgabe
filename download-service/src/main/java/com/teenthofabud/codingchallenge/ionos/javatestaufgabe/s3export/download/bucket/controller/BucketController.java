package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.controller;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketMessageTemplate;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.service.BucketService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("bucket")
@Slf4j
@Tag(name = "Bucket Search API", description = "Get buckets and their details")
public class BucketController {

    @Autowired
    public void setService(BucketService service) {
        this.service = service;
    }

    private BucketService service;
    
    @Operation(summary = "Get all Bucket details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available Buckets and their details",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BucketVo.class))) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Set<BucketVo> getAllBucketsNaturallyOrdered() throws BucketException {
        log.debug("Requesting all available Buckets by their natural orders");
        Set<BucketVo> naturallyOrderedBuckets = service.retrieveAllByNaturalOrdering();
        log.debug("Responding with all available Buckets by their natural orders");
        return naturallyOrderedBuckets;
    }

    @Operation(summary = "Get all Bucket details by country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrieve all available Buckets and their details that match the given country",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BucketVo.class))) }),
        @ApiResponse(responseCode = "400", description = "Bucket country is invalid",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
        @ApiResponse(responseCode = "404", description = "No Buckets available with the given country",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("country/{country}")
    public List<BucketVo> getAllBucketsByCountry(@PathVariable String country) throws BucketException {
        log.debug("Requesting all available Buckets with given country");
        if(StringUtils.hasText(StringUtils.trimWhitespace(country))) {
            List<BucketVo> matchedByCountry = service.retrieveAllForCountry(country);
            log.debug("Responding with all available Buckets with given country");
            return matchedByCountry;
        }
        log.debug("Bucket country is empty");
        throw new BucketException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "country", country });
    }

    @Operation(summary = "Get all countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available countries",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BucketVo.class))) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("country")
    public Set<String> getAllCountries() throws BucketException {
        Set<String> availableCountries = service.retrieveAllCountries();
        log.debug("Responding with all available country names");
        return availableCountries;
    }

    @Operation(summary = "Get all Bucket details by date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all available Buckets and their details that match the given date",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BucketVo.class))) }),
            @ApiResponse(responseCode = "400", description = "Bucket date is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No Buckets available with the given date",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("date/{date}")
    public List<BucketVo> getAllBucketsByDate(@Parameter(description = "yyyy-MM-dd") @PathVariable String date) throws BucketException {
        log.debug("Requesting all available Buckets with given date");
        if(StringUtils.hasText(StringUtils.trimWhitespace(date))) {
            List<BucketVo> matchedByDate = service.retrieveAllForDate(date);
            log.debug("Responding with all available Buckets with given date");
            return matchedByDate;
        }
        log.debug("Bucket date is empty");
        throw new BucketException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "date", date });
    }

    @Operation(summary = "Get Bucket details by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve the details of Bucket that matches the given ˇ",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BucketVo.class)) }),
            @ApiResponse(responseCode = "400", description = "Bucket name is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No Bucket found with the given name",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("name/{name}")
    public BucketVo getauditDetailsById(@Parameter(description = "s3export-synchronization-batch.<country-name>.<yyyy-MM-dd>")
                                            @PathVariable String name) throws BucketException {
        log.debug("Requesting all available Buckets by its name");
        if(StringUtils.hasText(StringUtils.trimWhitespace(name))) {
            BucketVo bucketDetails = service.retrieveByName(name);
            log.debug("Responding with successful retrieval of existing Bucket details by name");
            return bucketDetails;
        }
        log.debug(BucketMessageTemplate.MSG_TEMPLATE_BUCKET_NAME_EMPTY.getValue());
        throw new BucketException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "name", name });
    }

}
