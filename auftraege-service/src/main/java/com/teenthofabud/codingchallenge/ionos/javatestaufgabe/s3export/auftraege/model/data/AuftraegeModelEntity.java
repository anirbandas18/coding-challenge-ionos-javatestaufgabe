package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "auftraege")
public class AuftraegeModelEntity {

    @ToString.Include
    @EmbeddedId
    private AuftraegeModelPrimaryKey primaryKey;
    @ToString.Include
    @Column(name = "created")
    private String created;
    @ToString.Include
    @Column(name = "lastchange")
    private String lastChange;

}
