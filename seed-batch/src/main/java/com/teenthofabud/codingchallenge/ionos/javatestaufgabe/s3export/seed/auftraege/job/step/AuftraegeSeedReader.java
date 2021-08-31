package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelPrimaryKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@Slf4j
public class AuftraegeSeedReader implements ItemReader<AuftraegeModelEntity> {

    @Override
    public AuftraegeModelEntity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        AuftraegeModelEntity ae = new AuftraegeModelEntity();
        AuftraegeModelPrimaryKey pk = new AuftraegeModelPrimaryKey();
        ae.setPrimaryKey(pk);
        return ae;
    }
}
