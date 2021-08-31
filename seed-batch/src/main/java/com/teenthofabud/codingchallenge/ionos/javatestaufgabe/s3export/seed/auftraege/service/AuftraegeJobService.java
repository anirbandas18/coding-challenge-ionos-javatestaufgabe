package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedException;
import org.springframework.stereotype.Service;

@Service
public interface AuftraegeJobService {

    public void runJob() throws SeedException;

}
