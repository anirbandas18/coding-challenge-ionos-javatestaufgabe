package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.auftraege.data.AuftraegeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.AuftragKundeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
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
        List<AuftraegeModelVo> auftraegemodelVos =  filteringReader.read();
        List<AuftragKundeDto> auftragKundeDtos = filteringProcessor.process(auftraegemodelVos);
        filteringWriter.write(Arrays.asList(auftragKundeDtos));
        return RepeatStatus.FINISHED;
    }
}
