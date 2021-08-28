package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.data.entity.AuftraegeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.data.entity.AuftraegeEntityPrimaryKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@Slf4j
public class SeedReader implements ItemReader<AuftraegeEntity> {

    @Override
    public AuftraegeEntity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        AuftraegeEntity ae = new AuftraegeEntity();
        AuftraegeEntityPrimaryKey pk = new AuftraegeEntityPrimaryKey();
        ae.setId(pk);
        return ae;
    }
}
