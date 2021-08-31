package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class AuditForm {

    @ToString.Include
    private String module;
    @ToString.Include
    private String action;
    @ToString.Include
    private String description;
    @ToString.Include
    private String input;
    @ToString.Include
    private String output;
    @ToString.Include
    private Long userSequence;

}
