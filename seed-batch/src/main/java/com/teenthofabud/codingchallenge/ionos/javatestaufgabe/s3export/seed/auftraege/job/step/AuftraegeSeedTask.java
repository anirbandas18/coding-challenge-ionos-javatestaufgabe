package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AuftraegeSeedTask implements Tasklet, InitializingBean {

    private AuftraegeSeedReader reader;
    private AuftraegeSeedProcessor processor;
    private AuftraegeSeedWriter writer;
    private Integer batchSize;

    @Value("${s3export.seed.batch.size:10}")
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }


    @Autowired
    public void setReader(AuftraegeSeedReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(AuftraegeSeedProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(AuftraegeSeedWriter writer) {
        this.writer = writer;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<AuftraegeModelEntity> entities = new ArrayList<>(batchSize);
        for(int i = 0 ; i < batchSize ; i++) {
            AuftraegeModelEntity ae = reader.read();
            ae = processor.process(ae);
            entities.add(ae);
        }
        writer.write(entities);
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Batch size: {}", batchSize);
    }
}
