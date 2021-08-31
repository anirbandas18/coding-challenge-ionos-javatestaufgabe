package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.error;

import com.teenthofabud.core.common.error.TOABFeignException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KundeException extends TOABFeignException {

    public KundeException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public KundeException(String errorCode, String errorMessage, String errorDomain) {
        super(errorCode, errorMessage, errorDomain);
    }
}
