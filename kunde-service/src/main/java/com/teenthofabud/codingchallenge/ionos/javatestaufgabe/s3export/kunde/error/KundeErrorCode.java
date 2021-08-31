package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.error;

import com.teenthofabud.core.common.error.TOABError;

public enum KundeErrorCode implements TOABError {

    KUNDE_ATTRIBUTE_INVALID("S3E-KUNDE-001", 400), // syntactic
    KUNDE_NOT_FOUND("S3E-KUNDE-002", 404),
    KUNDE_ATTRIBUTE_UNEXPECTED("S3E-KUNDE-003", 422), // semantic
    KUNDE_EXISTS("S3E-KUNDE-004", 409),
    KUNDE_INACTIVE("S3E-KUNDE-005", 400),
    KUNDE_ACTION_FAILURE("S3E-KUNDE-006", 500);

    private String errorCode;
    private int httpStatusCode;

    private KundeErrorCode(String errorCode, int httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return "KundeErrorCode{" +
                this.name() + " -> " +
                "errorCode='" + errorCode + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                '}';
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public String getDomain() {
        return "Kunde";
    }

}
