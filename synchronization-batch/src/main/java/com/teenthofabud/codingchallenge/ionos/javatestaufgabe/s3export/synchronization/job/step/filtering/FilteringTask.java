package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class FilteringTask implements Tasklet {

    private FilteringReader reader;
    private FilteringProcessor processor;
    private FilteringWriter writer;

    @Autowired
    public void setReader(FilteringReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(FilteringProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(FilteringWriter writer) {
        this.writer = writer;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<AuftraegeEntity> auftraegeEntities =  reader.read();
        List<AuftragKundeDto> auftragKundeDtos = processor.process(auftraegeEntities);
        writer.write(Arrays.asList(auftragKundeDtos));
        return RepeatStatus.FINISHED;
    }
}
