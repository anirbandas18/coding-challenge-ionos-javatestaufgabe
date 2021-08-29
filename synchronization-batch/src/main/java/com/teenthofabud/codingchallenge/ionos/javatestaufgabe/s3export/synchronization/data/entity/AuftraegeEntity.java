package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "auftraege")
public class AuftraegeEntity {


    @ToString.Include
    @Column(name = "created")
    private String created;
    @ToString.Include
    @Column(name = "lastchange")
    private String lastChange;
    @ToString.Include
    @EmbeddedId
    private AuftraegeEntityPrimaryKey id;

}