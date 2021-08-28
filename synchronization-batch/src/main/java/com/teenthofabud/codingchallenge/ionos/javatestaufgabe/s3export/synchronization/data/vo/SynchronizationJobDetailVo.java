package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SynchronizationJobDetailVo {

    private Long id;
    private String name;
    private String instanceName;
    private Long instanceId;
    private String status;
    private Long executionId;
    private String startTime;
    private String createTime;

}
