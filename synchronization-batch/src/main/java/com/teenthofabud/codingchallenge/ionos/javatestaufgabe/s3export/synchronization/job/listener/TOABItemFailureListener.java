package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemListenerSupport;

import java.util.List;

@Slf4j
public class TOABItemFailureListener <I,O> extends ItemListenerSupport<I, O> {

    @Override
    public void onReadError(Exception ex) {
        log.error("Encountered error on read", ex);
    }

    @Override
    public void onWriteError(Exception ex, List<? extends O> item) {
        log.error("Encountered error on read", ex);
    }
}