package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;

public class SeedException extends TOABBaseException {
    public SeedException(String message) {
        super(message);
    }

    public SeedException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public SeedException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
    }

    public SeedException(TOABError error, String message) {
        super(error, message);
    }

    public SeedException(TOABError error, Object[] parameters) {
        super(error, parameters);
    }

    @Override
    public String getSubDomain() {
        return "job";
    }
}
