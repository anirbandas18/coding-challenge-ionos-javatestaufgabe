package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ToString
@RedisHash(value = "FileNameLocationCollection", timeToLive = 21600) // expire key after 6 hours
@TypeAlias("FileNameLocationCollection")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class FileBucketCollectionDto {

    @Id
    @Indexed
    @EqualsAndHashCode.Include
    private String collectionKey;
    private List<FileBucketDto> fileNameLocationMap;

    public String getCacheKey() {
        return "FileNameLocationCollection: " + collectionKey;
    }

    public FileBucketCollectionDto() {
        this.collectionKey = "";
        this.fileNameLocationMap = new LinkedList<>();
    }

}
