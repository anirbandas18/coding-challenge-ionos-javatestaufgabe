package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuftraegeModelVo implements Comparable<AuftraegeModelVo> {

    @ToString.Include
    private String kundenId;
    @ToString.Include
    private String auftragId;
    @ToString.Include
    private String artikelNummer;
    @ToString.Include
    private String created;
    @ToString.Include
    private String lastChange;

    @Override
    public int compareTo(AuftraegeModelVo o) {
        return this.auftragId.compareTo(o.getAuftragId());
    }
}
