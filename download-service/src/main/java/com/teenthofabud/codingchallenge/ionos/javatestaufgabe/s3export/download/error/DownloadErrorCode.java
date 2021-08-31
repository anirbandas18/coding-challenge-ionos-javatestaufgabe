package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error;

import com.teenthofabud.core.common.error.TOABError;

public enum DownloadErrorCode implements TOABError {

    DOWNLOAD_ATTRIBUTE_INVALID("S3E-DOWNLOAD-001", 400), // syntactic
    DOWNLOAD_NOT_FOUND("S3E-DOWNLOAD-002", 404),
    DOWNLOAD_ATTRIBUTE_UNEXPECTED("S3E-DOWNLOAD-003", 422), // semantic
    DOWNLOAD_EXISTS("S3E-DOWNLOAD-004", 409),
    DOWNLOAD_INACTIVE("S3E-DOWNLOAD-005", 400),
    DOWNLOAD_ACTION_FAILURE("S3E-DOWNLOAD-006", 500);

    private String errorCode;
    private int httpStatusCode;

    private DownloadErrorCode(String errorCode, int httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return "DownloadErrorCode{" +
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
        return "Download";
    }

}
