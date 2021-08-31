package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AuftraegeModelVo implements Comparable<AuftraegeModelVo> {

    @ToString.Include
    private String kundenId;
    @ToString.Include
    private String auftraegeId;
    @ToString.Include
    private String artikelNummer;
    @ToString.Include
    private String created;
    @ToString.Include
    private String lastChange;

    @Override
    public int compareTo(AuftraegeModelVo o) {
        return this.auftraegeId.compareTo(o.getAuftraegeId());
    }

    public AuftraegeModelVo() {
        this.auftraegeId = "";
        this.artikelNummer = "";
        this.kundenId = "";
        this.created = "";
        this.lastChange = "";
    }
}
