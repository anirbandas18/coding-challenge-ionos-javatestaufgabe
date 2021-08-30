package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BucketVo {

    @EqualsAndHashCode.Include
    private String name;

}
