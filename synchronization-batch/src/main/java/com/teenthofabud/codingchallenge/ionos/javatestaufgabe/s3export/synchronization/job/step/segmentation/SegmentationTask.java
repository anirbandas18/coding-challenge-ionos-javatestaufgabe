package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.LandKundenDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.LandKundeAuftragCollectionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SegmentationTask implements Tasklet {

    private SegmentationReader reader;
    private SegmentationProcessor processor;
    private SegmentationWriter writer;

    @Autowired
    public void setReader(SegmentationReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(SegmentationProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(SegmentationWriter writer) {
        this.writer = writer;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<LandKundenDto> landKundenDtos = reader.read();
        List<LandKundeAuftragCollectionVo> landKundeAuftragCollectionVos = processor.process(landKundenDtos);
        writer.write(Arrays.asList(landKundeAuftragCollectionVos));
        return RepeatStatus.FINISHED;
    }
}
