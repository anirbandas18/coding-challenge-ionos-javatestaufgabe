package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuditVo implements Comparable<AuditVo> {

    @ToString.Include
    private Long id;
    @ToString.Include
    private Boolean active;
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
    private String createdAt;
    @ToString.Include
    private String createdBy;
    @ToString.Include
    private String modifiedAt;
    @ToString.Include
    private String modifiedBy;

    @Override
    public int compareTo(AuditVo o) {
        return Long.compare(this.id, o.getId());
    }
}
