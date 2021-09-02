package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.service;

import com.teenthofabud.core.common.error.TOABSystemException;
import org.springframework.stereotype.Service;

@Service
public interface AuftraegeJobService {

    public void runJob() throws TOABSystemException;

}
