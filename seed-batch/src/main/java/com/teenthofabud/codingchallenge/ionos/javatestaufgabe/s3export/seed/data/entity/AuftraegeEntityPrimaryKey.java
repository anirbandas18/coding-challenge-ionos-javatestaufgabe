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
public class AuftraegeEntityPrimaryKey implements Serializable {

    @ToString.Include
    @Column(name = "auftragid")
    private String auftragId;
    @ToString.Include
    @Column(name = "artikelnummer")
    private String artikelNummber;
    @ToString.Include
    @Column(name = "kundeid")
    private String kundenId;

}