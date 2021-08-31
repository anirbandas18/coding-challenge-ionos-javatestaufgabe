package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.data;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class KundeModelForm {

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

}
