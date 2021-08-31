package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.error;

import com.teenthofabud.core.common.error.TOABError;

public enum AuftraegeErrorCode implements TOABError {

    AUFTRAEGE_ATTRIBUTE_INVALID("S3E-AUFTRAEGE-001", 400), // syntactic
    AUFTRAEGE_NOT_FOUND("S3E-AUFTRAEGE-002", 404),
    AUFTRAEGE_ATTRIBUTE_UNEXPECTED("S3E-AUFTRAEGE-003", 422), // semantic
    AUFTRAEGE_EXISTS("S3E-AUFTRAEGE-004", 409),
    AUFTRAEGE_INACTIVE("S3E-AUFTRAEGE-005", 400),
    AUFTRAEGE_ACTION_FAILURE("S3E-AUFTRAEGE-006", 500);

    private String errorCode;
    private int httpStatusCode;

    private AuftraegeErrorCode(String errorCode, int httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return "AuftraegeErrorCode{" +
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
        return "Auftraege";
    }

}
