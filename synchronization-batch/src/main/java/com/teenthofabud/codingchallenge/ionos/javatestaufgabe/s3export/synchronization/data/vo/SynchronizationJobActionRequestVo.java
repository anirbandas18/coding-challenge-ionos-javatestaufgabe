package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SynchronizationJobActionRequestVo {

    private String action;
    private String status;

}
