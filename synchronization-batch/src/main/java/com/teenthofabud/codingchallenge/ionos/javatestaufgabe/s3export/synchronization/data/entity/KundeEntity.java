package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "kunde")
public class KundeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @Column(name = "kundenid")
    private Long kundenId;
    @ToString.Include
    @Column(name = "vorname")
    private String vorName;
    @ToString.Include
    @Column(name = "nachname")
    private String nachName;
    @ToString.Include
    private String email;
    @ToString.Include
    private String strasse;
    @ToString.Include
    @Column(name = "strassenzusatz")
    private String strassenZuSatz;
    @ToString.Include
    private String ort;
    @ToString.Include
    private String land;
    @ToString.Include
    private String plz;
    @ToString.Include
    @Column(name = "firmenname")
    private String firmenName;

}