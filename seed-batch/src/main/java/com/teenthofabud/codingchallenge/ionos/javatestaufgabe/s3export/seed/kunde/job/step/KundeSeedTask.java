package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.data.KundeModelEntity;
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
public class KundeSeedTask implements Tasklet, InitializingBean {

    private KundeSeedReader reader;
    private KundeSeedProcessor processor;
    private KundeSeedWriter writer;
    private Integer batchSize;

    @Value("${s3export.seed.batch.kunde.size:100}")
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }


    @Autowired
    public void setReader(KundeSeedReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(KundeSeedProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(KundeSeedWriter writer) {
        this.writer = writer;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<KundeModelEntity> entities = new ArrayList<>(batchSize);
        for(int i = 0 ; i < batchSize ; i++) {
            KundeModelEntity ke = reader.read();
            ke = processor.process(ke);
            entities.add(ke);
        }
        writer.write(entities);
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Batch size: {}", batchSize);
    }
}
