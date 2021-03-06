package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data;

import lombok.*;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class FileDetailEntity {

    @ToString.Include
    private String name;
    @ToString.Include
    private String country;
    @ToString.Include
    private LocalDateTime dateTime;

}
