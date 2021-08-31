package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data;

import lombok.Getter;

@Getter
public enum KundeModelMessageTemplate {

    MSG_TEMPLATE_SEARCHING_FOR_MODEL_ENTITY_ID("Searching for KundeModelEntity with id: {}"),
    MSG_TEMPLATE_NO_MODEL_ENTITY_ID_AVAILABLE("No KundeModelEntity available with id: {}"),
    MSG_TEMPLATE_FOUND_MODEL_ENTITY_ID("Found KundeModelEntity with id: {}"),
    MSG_TEMPLATE_MODEL_ID_VALID("kunde model id: {} is semantically valid"),
    MSG_TEMPLATE_MODEL_ID_INVALID("kunde model id: {} is invalid"),
    MSG_TEMPLATE_MODEL_ID_EMPTY("kunde model id is empty"),
    MSG_TEMPLATE_MODEL_EXISTENCE_BY_EMAIL ("Checking existence of KundeModelEntity with email: {}"),
    MSG_TEMPLATE_MODEL_EXISTS_BY_EMAIL ("KundeModelEntity already exists with email: {}"),
    MSG_TEMPLATE_MODEL_NON_EXISTENCE_BY_EMAIL ("No KundeModelEntity exists with email: {}");


    private String value;

    private KundeModelMessageTemplate(String value) {
        this.value = value;
    }


}
