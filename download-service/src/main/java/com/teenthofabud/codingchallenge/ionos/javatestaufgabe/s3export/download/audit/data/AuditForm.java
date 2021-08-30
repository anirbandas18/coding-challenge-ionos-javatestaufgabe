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

}
