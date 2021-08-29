package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragDto;
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

    private MappingReader reader;
    private MappingProcessor processor;
    private MappingWriter writer;

    @Autowired
    public void setReader(MappingReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(MappingProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(MappingWriter writer) {
        this.writer = writer;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<AuftragKundeDto> auftragKundeDtos = reader.read();
        List<KundeAuftragDto> kundeAuftragDtos = processor.process(auftragKundeDtos);
        writer.write(Arrays.asList(kundeAuftragDtos));
        return RepeatStatus.FINISHED;
    }
}
