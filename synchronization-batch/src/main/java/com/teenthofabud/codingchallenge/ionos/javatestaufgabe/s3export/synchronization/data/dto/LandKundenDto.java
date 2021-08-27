package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RedisHash("LandKundenMapDto")
@TypeAlias("LandKundenMapDto")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LandKundenDto {

    @ToString.Include
    private List<KundeAuftragDto> associatedKundenAuftrag;
    @Id
    @Indexed
    @ToString.Include
    private String land;

    public String getCacheKey() {
        return this.getClass().getSimpleName() + ": " + land;
    }

}
