package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error;

import com.teenthofabud.core.common.error.TOABError;

public enum SeedErrorCode implements TOABError {

    SEED_ATTRIBUTE_INVALID("S3E-SEED-001", 400), // syntactic
    SEED_NOT_FOUND("S3E-SEED-002", 404),
    SEED_ATTRIBUTE_UNEXPECTED("S3E-SEED-003", 422), // semantic
    SEED_EXISTS("S3E-SEED-004", 409),
    SEED_INACTIVE("S3E-SEED-005", 400),
    SEED_ACTION_FAILURE("S3E-SEED-006", 500);

    private String errorCode;
    private int httpStatusCode;

    private SeedErrorCode(String errorCode, int httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return "SeedErrorCode{" +
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
        return "Seed";
    }

}
