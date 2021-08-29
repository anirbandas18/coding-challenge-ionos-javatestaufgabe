package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuftraegeEntityPrimaryKey implements Serializable {

    @ToString.Include
    @Column(name = "auftragid")
    @EqualsAndHashCode.Include
    private String auftragId;
    @ToString.Include
    @Column(name = "artikelnummer")
    @EqualsAndHashCode.Include
    private String artikelNummber;
    @ToString.Include
    @Column(name = "kundeid")
    @EqualsAndHashCode.Include
    private String kundenId;

}