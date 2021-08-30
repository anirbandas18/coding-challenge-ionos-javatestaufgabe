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

    private FilteringReader filteringReader;
    private FilteringProcessor filteringProcessor;
    private FilteringWriter filteringWriter;

    @Autowired
    public void setFilteringReader(FilteringReader filteringReader) {
        this.filteringReader = filteringReader;
    }

    @Autowired
    public void setFilteringProcessor(FilteringProcessor filteringProcessor) {
        this.filteringProcessor = filteringProcessor;
    }

    @Autowired
    public void setFilteringWriter(FilteringWriter filteringWriter) {
        this.filteringWriter = filteringWriter;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<AuftraegeEntity> auftraegeEntities =  filteringReader.read();
        List<AuftragKundeDto> auftragKundeDtos = filteringProcessor.process(auftraegeEntities);
        filteringWriter.write(Arrays.asList(auftragKundeDtos));
        return RepeatStatus.FINISHED;
    }
}
