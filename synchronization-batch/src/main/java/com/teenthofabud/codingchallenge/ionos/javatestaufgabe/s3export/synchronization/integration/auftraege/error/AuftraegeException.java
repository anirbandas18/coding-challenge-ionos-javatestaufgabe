package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.error;

import com.teenthofabud.core.common.error.TOABFeignException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuftraegeException extends TOABFeignException {

    public AuftraegeException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public AuftraegeException(String errorCode, String errorMessage, String errorDomain) {
        super(errorCode, errorMessage, errorDomain);
    }
}
