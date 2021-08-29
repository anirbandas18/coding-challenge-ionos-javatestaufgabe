package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class FileBucketDto {

    @EqualsAndHashCode.Include
    private String fileName;
    @EqualsAndHashCode.Include
    private String bucketName;
    @EqualsAndHashCode.Include
    private String fileLocation;

}
