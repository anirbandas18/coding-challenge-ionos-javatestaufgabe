package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data;

import com.teenthofabud.core.common.data.entity.TOABBaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_event")
@ToString
public class AuditEntity extends TOABBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;
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

}
