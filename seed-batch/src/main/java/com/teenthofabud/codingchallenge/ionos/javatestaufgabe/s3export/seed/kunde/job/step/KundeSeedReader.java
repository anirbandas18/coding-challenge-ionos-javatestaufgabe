package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.data.KundeModelEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@Slf4j
public class KundeSeedReader implements ItemReader<KundeModelEntity> {

    @Override
    public KundeModelEntity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return new KundeModelEntity();
    }
}
