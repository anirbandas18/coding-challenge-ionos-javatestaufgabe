package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.error.SynchronizationException;
import org.springframework.stereotype.Service;

@Service
public interface SynchronizationJobService {

    public void runJob() throws SynchronizationException;

}
