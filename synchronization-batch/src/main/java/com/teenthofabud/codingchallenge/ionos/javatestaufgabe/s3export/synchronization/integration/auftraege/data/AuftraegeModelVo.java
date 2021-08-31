package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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

    public AuftraegeModelVo() {
        this.auftragId = "";
        this.artikelNummer = "";
        this.kundenId = "";
        this.created = "";
        this.lastChange = "";
    }
}
