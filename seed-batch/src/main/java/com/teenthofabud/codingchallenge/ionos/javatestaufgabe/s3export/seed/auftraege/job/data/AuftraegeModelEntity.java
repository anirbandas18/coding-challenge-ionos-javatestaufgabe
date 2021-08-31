package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "auftraege")
public class AuftraegeModelEntity {


    @ToString.Include
    @Column(name = "created")
    private String created;
    @ToString.Include
    @Column(name = "lastchange")
    private String lastChange;
    @ToString.Include
    @EmbeddedId
    private AuftraegeModelPrimaryKey primaryKey;

}