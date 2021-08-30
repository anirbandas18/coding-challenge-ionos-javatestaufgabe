package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data;

import lombok.Getter;

@Getter
public enum BucketMessageTemplate {

    MSG_TEMPLATE_SEARCHING_FOR_BUCKET_ENTITY_NAME("Searching for BUCKETEntity with name: {}"),
    MSG_TEMPLATE_NO_BUCKET_ENTITY_NAME_AVAILABLE("No BUCKETEntity available with name: {}"),
    MSG_TEMPLATE_FOUND_BUCKET_ENTITY_NAME("Found BUCKETEntity with name: {}"),
    MSG_TEMPLATE_BUCKET_NAME_VALID("BUCKET name: {} is semantically valid"),
    MSG_TEMPLATE_BUCKET_NAME_INVALID("BUCKET name: {} is invalid"),
    MSG_TEMPLATE_BUCKET_NAME_EMPTY("BUCKET name is empty"),
    MSG_TEMPLATE_BUCKET_EXISTENCE_BY_NAME ("Checking existence of BUCKETEntity with name: {}"),
    MSG_TEMPLATE_BUCKET_EXISTS_BY_NAME ("BUCKETEntity already exists with name: {}"),
    MSG_TEMPLATE_BUCKET_NON_EXISTENCE_BY_NAME ("No BUCKETEntity exists with name: {}");

    private String value;

    private BucketMessageTemplate(String value) {
        this.value = value;
    }

}
