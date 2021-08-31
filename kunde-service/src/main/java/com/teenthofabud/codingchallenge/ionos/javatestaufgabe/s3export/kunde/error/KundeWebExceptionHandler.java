package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.error;

import brave.Tracer;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelException;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.handler.TOABBaseWebExceptionHandler;
import com.teenthofabud.core.common.handler.TOABMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class KundeWebExceptionHandler implements TOABBaseWebExceptionHandler {

    @Autowired
    public void setMessageSource(TOABMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private TOABMessageSource messageSource;

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    private Tracer tracer;

    @ExceptionHandler(value = { KundeModelException.class })
    public ResponseEntity<ErrorVo> handleKundeSubDomainExceptions(TOABBaseException e) {
        ResponseEntity<ErrorVo>  response = parseExceptionToResponse(e, messageSource, tracer);
        return response;
    }

}
