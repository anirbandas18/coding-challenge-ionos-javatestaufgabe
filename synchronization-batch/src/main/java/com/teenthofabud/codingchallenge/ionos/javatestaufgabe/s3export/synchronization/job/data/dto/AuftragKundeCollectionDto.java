package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto;


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
@RedisHash(value = "AuftragKundeCollection", timeToLive = 21600) // expire key after 6 hours
@TypeAlias("AuftragKundeCollection")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class AuftragKundeCollectionDto {

    @Id
    @Indexed
    @EqualsAndHashCode.Include
    private String collectionKey;
    private List<? extends AuftragKundeDto> auftragKundeMap;

    public String getCacheKey() {
        return "AuftragKundeCollection: " + collectionKey;
    }

    public AuftragKundeCollectionDto() {
        this.collectionKey = "";
        this.auftragKundeMap = new LinkedList<>();
    }

}
