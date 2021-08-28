package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.data.entity.AuftraegeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.repository.AuftraegeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class SeedWriter implements ItemWriter<AuftraegeEntity> {

    private AuftraegeRepository repository;

    @Autowired
    public void setRepository(AuftraegeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(List<? extends AuftraegeEntity> items) throws Exception {
        int count = 0;
        for(AuftraegeEntity entity : items) {
            if(repository.existsByIdAuftragId(entity.getId().getAuftragId())) {
                log.info("{} skipped due to conflict with existing data having auftraegeId: {}", entity, entity.getId().getAuftragId());
            } else {
                repository.save(entity);
                log.debug("Saved {}", entity);
                count++;
            }
        }
        log.info("Saved {} auftraege entities", count);
    }
}
