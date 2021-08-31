package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.error;

import com.teenthofabud.core.common.error.TOABError;

public enum SynchronizationErrorCode implements TOABError {

    SYNCHRONIZATION_ATTRIBUTE_INVALID("S3E-SYNCHRONIZATION-001", 400), // syntactic
    SYNCHRONIZATION_NOT_FOUND("S3E-SYNCHRONIZATION-002", 404),
    SYNCHRONIZATION_ATTRIBUTE_UNEXPECTED("S3E-SYNCHRONIZATION-003", 422), // semantic
    SYNCHRONIZATION_EXISTS("S3E-SYNCHRONIZATION-004", 409),
    SYNCHRONIZATION_INACTIVE("S3E-SYNCHRONIZATION-005", 400),
    SYNCHRONIZATION_ACTION_FAILURE("S3E-SYNCHRONIZATION-006", 500);

    private String errorCode;
    private int httpStatusCode;

    private SynchronizationErrorCode(String errorCode, int httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return "SynchronizationErrorCode{" +
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
        return "Synchronization";
    }

}
