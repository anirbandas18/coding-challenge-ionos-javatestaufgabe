package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Set;

@Getter
@Setter
@ToString
@RedisHash(value = "LandCollection", timeToLive = 21600) // expire key after 6 hours
@TypeAlias("LandCollection")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class LandCollectionDto {

    @Id
    @Indexed
    @EqualsAndHashCode.Include
    private String collectionKey;
    private Set<? extends String> distinctLand;

    public String getCacheKey() {
        return "LandCollection: " + collectionKey;
    }

}
