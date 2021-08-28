/*
package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.health;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.SynchronizationJobDetailVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.service.SynchronizationJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "jobStatus")
public class SynchronizationJobStatusEndpoint {

    private SynchronizationJobService service;

    @Autowired
    public void setService(SynchronizationJobService service) {
        this.service = service;
    }

    @ReadOperation
    public ResponseEntity<SynchronizationJobDetailVo> invoke() {
        SynchronizationJobDetailVo vo = service.jobStatus();
        return new ResponseEntity<>(vo, HttpStatus.OK);

    }

}
*/
