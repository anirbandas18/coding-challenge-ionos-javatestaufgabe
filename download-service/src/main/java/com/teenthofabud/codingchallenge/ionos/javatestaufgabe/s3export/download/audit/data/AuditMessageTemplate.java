package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data;

import lombok.Getter;

@Getter
public enum AuditMessageTemplate {

    MSG_TEMPLATE_SEARCHING_FOR_AUDIT_ENTITY_ID("Searching for AuditEntity with id: {}"),
    MSG_TEMPLATE_NO_AUDIT_ENTITY_ID_AVAILABLE("No AuditEntity available with id: {}"),
    MSG_TEMPLATE_FOUND_AUDIT_ENTITY_ID("Found AuditEntity with id: {}"),
    MSG_TEMPLATE_AUDIT_ID_VALID("Audit id: {} is semantically valid"),
    MSG_TEMPLATE_AUDIT_ID_INVALID("Audit id: {} is invalid"),
    MSG_TEMPLATE_AUDIT_ID_EMPTY("Audit id is empty");

    private String value;

    private AuditMessageTemplate(String value) {
        this.value = value;
    }

}
