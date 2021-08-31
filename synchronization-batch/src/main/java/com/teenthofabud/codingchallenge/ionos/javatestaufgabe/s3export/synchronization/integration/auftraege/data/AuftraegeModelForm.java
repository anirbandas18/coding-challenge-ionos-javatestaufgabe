package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class AuftraegeModelForm {


    @ToString.Include
    private String artikelNummer;
    @ToString.Include
    private String kundenId;
    @ToString.Include
    private String auftragId;

}
