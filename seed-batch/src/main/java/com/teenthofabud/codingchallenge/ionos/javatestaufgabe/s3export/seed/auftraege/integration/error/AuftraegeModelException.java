package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.error;

import com.teenthofabud.core.common.error.TOABFeignException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuftraegeModelException extends TOABFeignException {

    public AuftraegeModelException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public AuftraegeModelException(String errorCode, String errorMessage, String errorDomain) {
        super(errorCode, errorMessage, errorDomain);
    }
}
