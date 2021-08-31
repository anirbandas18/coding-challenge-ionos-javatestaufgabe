package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data;

import lombok.Getter;

@Getter
public enum AuftraegeModelMessageTemplate {

    MSG_TEMPLATE_SEARCHING_FOR_MODEL_ENTITY_ID("Searching for AuftraegeModelEntity with id: {}"),
    MSG_TEMPLATE_NO_MODEL_ENTITY_ID_AVAILABLE("No AuftraegeModelEntity available with id: {}"),
    MSG_TEMPLATE_FOUND_MODEL_ENTITY_ID("Found AuftraegeModelEntity with id: {}"),

    MSG_TEMPLATE_MODEL_ID_VALID("Auftraege model id: {} is semantically valid"),
    MSG_TEMPLATE_MODEL_ID_INVALID("Auftraege model id: {} is invalid"),
    MSG_TEMPLATE_MODEL_ID_EMPTY("Auftraege model id is empty"),

    MSG_TEMPLATE_MODEL_ARTIKEL_NUMMER_VALID("Auftraege artikel nummer: {} is semantically valid"),
    MSG_TEMPLATE_MODEL_ARTIKEL_NUMMER_INVALID("Auftraege artikel nummer: {} is invalid"),
    MSG_TEMPLATE_MODEL_ARTIKEL_NUMMER_EMPTY("Auftraege artikel nummer is empty"),

    MSG_TEMPLATE_MODEL_KUNDEN_ID_VALID("Auftraege model kunden id: {} is semantically valid"),
    MSG_TEMPLATE_MODEL_KUNDEN_ID_INVALID("Auftraege model kunden id: {} is invalid"),
    MSG_TEMPLATE_MODEL_KUNDEN_ID_EMPTY("Auftraege model kunden id is empty"),

    MSG_TEMPLATE_MODEL_EXISTENCE_BY_ID_ARTIKEL_NUMMER_KUNDE_ID ("Checking existence of AuftraegeModelEntity with auftraege id: {}, artikel nummer: {}, kunde id: {}"),
    MSG_TEMPLATE_MODEL_EXISTS_BY_ID_ARTIKEL_NUMMER_KUNDE_ID ("AuftraegeModelEntity already exists with auftraege id: {}, artikel nummer: {}, kunde id: {}"),
    MSG_TEMPLATE_MODEL_NON_EXISTENCE_BY_ID_ARTIKEL_NUMMER_KUNDE_ID ("No AuftraegeModelEntity exists with auftraege id: {}, artikel nummer: {}, kunde id: {}");


    private String value;

    private AuftraegeModelMessageTemplate(String value) {
        this.value = value;
    }


}
