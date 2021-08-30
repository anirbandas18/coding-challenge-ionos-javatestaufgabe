package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data;

import com.google.common.net.MediaType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class FileVo {

    @EqualsAndHashCode.Include
    private String name;
    private byte[] content;
    @EqualsAndHashCode.Include
    private String type;

}
