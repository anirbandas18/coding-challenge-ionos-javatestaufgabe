package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error.SeedException;
import org.springframework.stereotype.Service;

@Service
public interface KundeJobService {

    public void runJob() throws SeedException;

}
