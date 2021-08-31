package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.KundeAuftragDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class MappingTask implements Tasklet {

    private MappingReader mappingreader;
    private MappingProcessor mappingProcessor;
    private MappingWriter mappingWriter;

    @Autowired
    public void setMappingreader(MappingReader mappingreader) {
        this.mappingreader = mappingreader;
    }

    @Autowired
    public void setMappingProcessor(MappingProcessor mappingProcessor) {
        this.mappingProcessor = mappingProcessor;
    }

    @Autowired
    public void setMappingWriter(MappingWriter mappingWriter) {
        this.mappingWriter = mappingWriter;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<AuftragKundeDto> auftragKundeDtos = mappingreader.read();
        List<KundeAuftragDto> kundeAuftragDtos = mappingProcessor.process(auftragKundeDtos);
        mappingWriter.write(Arrays.asList(kundeAuftragDtos));
        return RepeatStatus.FINISHED;
    }
}
