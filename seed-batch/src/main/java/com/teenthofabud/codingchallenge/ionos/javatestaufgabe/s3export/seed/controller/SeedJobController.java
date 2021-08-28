package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.controller;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.service.SeedJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job")
public class SeedJobController {

    private SeedJobService service;

    @Autowired
    public void setService(SeedJobService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> postStartRequest() {
        service.startJob();
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getStopRequest() {
        service.stopJob();
        return ResponseEntity.noContent().build();
    }

}
