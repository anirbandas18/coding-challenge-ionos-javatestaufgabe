package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.core.common.error.TOABErrorCode;
import com.teenthofabud.core.common.error.TOABSystemException;
import com.teenthofabud.core.common.proxy.TOABFeignBaseExceptionHandler;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Slf4j
public class AuftraegeServiceClientExceptionHandler implements TOABFeignBaseExceptionHandler {

    @Autowired
    public void setOm(ObjectMapper om) {
        this.om = om;
    }

    private ObjectMapper om;

    @Override
    public Optional<AuftraegeException> parseResponseToException(Response response) {
        Optional<AuftraegeException> optEx = Optional.empty();
        try {
            HttpStatus httpStatusCode = HttpStatus.resolve(response.status());
            InputStream rawErrorResponseBody = response.body().asInputStream();
            byte[] rawResponse = rawErrorResponseBody.readAllBytes();
            String formattedResponse = new String(rawResponse, StandardCharsets.UTF_8);
            log.error("Response from service: {}", formattedResponse);
            ErrorVo errorDetails = om.readValue(rawResponse, ErrorVo.class);

            if (HttpStatus.BAD_REQUEST.equals(httpStatusCode)
                    //|| HttpStatus.NOT_FOUND.equals(httpStatusCode)
                    || HttpStatus.CONFLICT.equals(httpStatusCode)
                    || HttpStatus.UNPROCESSABLE_ENTITY.equals(httpStatusCode)
                    || HttpStatus.INTERNAL_SERVER_ERROR.equals(httpStatusCode)) {
                optEx = Optional.of(new AuftraegeException(errorDetails.getCode(), errorDetails.getMessage(), errorDetails.getDomain()));
            } else {
                throw new TOABSystemException(TOABErrorCode.UNEXPECTED_CLIENT_RESPONSE_STATUS, errorDetails.getMessage());
            }

            return optEx;
        } catch (IOException e) {
            log.error("Unable to parse response body", e);
            throw new TOABSystemException(TOABErrorCode.SYSTEM_IO_FAILURE, "Unable to parse response body", e);
        }
    }
}
