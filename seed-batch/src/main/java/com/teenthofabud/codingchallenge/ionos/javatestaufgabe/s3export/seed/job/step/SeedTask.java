package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.data.entity.AuftraegeEntity;
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
public class SeedTask implements Tasklet, InitializingBean {

    private SeedReader reader;
    private SeedProcessor processor;
    private SeedWriter writer;
    private Integer batchSize;

    @Value("${s3export.seed.batch.size:10}")
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }


    @Autowired
    public void setReader(SeedReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(SeedProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(SeedWriter writer) {
        this.writer = writer;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<AuftraegeEntity> entities = new ArrayList<>(batchSize);
        for(int i = 0 ; i < batchSize ; i++) {
            AuftraegeEntity ae = reader.read();
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
