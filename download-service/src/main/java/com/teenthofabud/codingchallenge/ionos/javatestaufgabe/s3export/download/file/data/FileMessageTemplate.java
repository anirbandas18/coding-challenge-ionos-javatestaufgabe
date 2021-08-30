package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data;

import lombok.Getter;

@Getter
public enum FileMessageTemplate {

    MSG_TEMPLATE_SEARCHING_FOR_FILE_ENTITY_NAME("Searching for FILEEntity with name: {}"),
    MSG_TEMPLATE_NO_FILE_ENTITY_NAME_AVAILABLE("No FILEEntity available with name: {}"),
    MSG_TEMPLATE_FOUND_FILE_ENTITY_NAME("Found FILEEntity with name: {}"),
    MSG_TEMPLATE_FILE_NAME_VALID("FILE name: {} is semantically valid"),
    MSG_TEMPLATE_FILE_NAME_INVALID("FILE name: {} is invalid"),
    MSG_TEMPLATE_FILE_NAME_EMPTY("FILE name is empty"),
    MSG_TEMPLATE_FILE_EXISTENCE_BY_NAME ("Checking existence of FILEEntity with name: {}"),
    MSG_TEMPLATE_FILE_EXISTS_BY_NAME ("FILEEntity already exists with name: {}"),
    MSG_TEMPLATE_FILE_NON_EXISTENCE_BY_NAME ("No FILEEntity exists with name: {}");

    private String value;

    private FileMessageTemplate(String value) {
        this.value = value;
    }

}
