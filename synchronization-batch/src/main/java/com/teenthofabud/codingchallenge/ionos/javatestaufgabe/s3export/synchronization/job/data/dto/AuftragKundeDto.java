package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class AuftragKundeDto {

    @EqualsAndHashCode.Include
    private String auftragId;
    @EqualsAndHashCode.Include
    private String artikelNummer;
    @EqualsAndHashCode.Include
    private Long kundeId;

}
