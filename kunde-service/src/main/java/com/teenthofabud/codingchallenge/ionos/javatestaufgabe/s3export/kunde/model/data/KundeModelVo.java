package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KundeModelVo implements Comparable<KundeModelVo> {

    private Long kundenId;
    @ToString.Include
    private String vorName;
    @ToString.Include
    private String nachName;
    @ToString.Include
    private String email;
    @ToString.Include
    private String strasse;
    @ToString.Include
    private String strassenZuSatz;
    @ToString.Include
    private String ort;
    @ToString.Include
    private String land;
    @ToString.Include
    private String plz;
    @ToString.Include
    private String firmenName;

    @Override
    public int compareTo(KundeModelVo o) {
        return this.kundenId.compareTo(o.getKundenId());
    }
}
