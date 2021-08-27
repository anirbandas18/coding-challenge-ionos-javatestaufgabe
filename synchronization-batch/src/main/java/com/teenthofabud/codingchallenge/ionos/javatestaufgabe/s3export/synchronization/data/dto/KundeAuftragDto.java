package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class KundeAuftragDto {

    private static final String CSV_SEPARATOR = ",";

    private String firma;
    private String strasse;
    private String strassenZuSatz;
    private String ort;
    private String land;
    private String plz;
    private String vorName;
    private String nachName;
    @EqualsAndHashCode.Include
    private String kundeId;
    @EqualsAndHashCode.Include
    private String auftragId;
    @EqualsAndHashCode.Include
    private String artikelNummer;

}
