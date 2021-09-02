package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.service;
import com.teenthofabud.core.common.error.TOABSystemException;
import org.springframework.stereotype.Service;

@Service
public interface KundeJobService {

    public void runJob() throws TOABSystemException;

}
