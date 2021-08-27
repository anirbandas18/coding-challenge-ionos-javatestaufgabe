package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity;

import com.teenthofabud.core.common.data.entity.TOABBaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "auftraege")
public class AuftraegeEntity extends TOABBaseEntity {


    @ToString.Include
    @Column(name = "created")
    private String created;
    @ToString.Include
    @Column(name = "lastchanged")
    private String lastChange;
    @ToString.Include
    @EmbeddedId
    private AuftraegeEntityPrimaryKey id;

}