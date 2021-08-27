package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@Getter
@Setter
@ToString
@RedisHash(value = "KundeAuftragCollection", timeToLive = 21600) // expire key after 6 hours
@TypeAlias("KundeAuftragCollection")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class KundeAuftragCollectionDto {

    @Id
    @Indexed
    @EqualsAndHashCode.Include
    private String collectionKey;
    private List<? extends KundeAuftragDto> kundeAuftragMap;

    public String getCacheKey() {
        return "KundeAuftragCollection: " + collectionKey;
    }

}
