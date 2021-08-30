package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data;

import lombok.*;

import java.io.InputStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class FileEntity {

    private InputStream filePointer;
    @ToString.Include
    private String name;

}
