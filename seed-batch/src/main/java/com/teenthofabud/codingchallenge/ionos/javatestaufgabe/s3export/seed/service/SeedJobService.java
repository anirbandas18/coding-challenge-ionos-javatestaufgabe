package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedException;
import org.springframework.stereotype.Service;

@Service
public interface SeedJobService {

    public void runJob() throws SeedException;

    public void startJob();

    public void stopJob();

}
