package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BucketEntity {

    private String prefix;
    private String country;
    private LocalDate date;
    private String name;

}
