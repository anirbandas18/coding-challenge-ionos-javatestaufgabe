package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class LandKundeAuftragCollectionVo {

    @ToString.Include
    private String land;
    @ToString.Include
    private Long timestamp;
    private List<KundeAuftragVo> items;

}
