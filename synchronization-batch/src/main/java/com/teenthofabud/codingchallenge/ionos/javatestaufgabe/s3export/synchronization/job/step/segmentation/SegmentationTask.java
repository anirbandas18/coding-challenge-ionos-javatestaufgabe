package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.LandKundenDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.vo.LandKundeAuftragCollectionVo;
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

    private SegmentationReader segmentationReader;
    private SegmentationProcessor segmentationProcessor;
    private SegmentationWriter segmentationWriter;

    @Autowired
    public void setSegmentationReader(SegmentationReader segmentationReader) {
        this.segmentationReader = segmentationReader;
    }

    @Autowired
    public void setSegmentationProcessor(SegmentationProcessor segmentationProcessor) {
        this.segmentationProcessor = segmentationProcessor;
    }

    @Autowired
    public void setSegmentationWriter(SegmentationWriter segmentationWriter) {
        this.segmentationWriter = segmentationWriter;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<LandKundenDto> landKundenDtos = segmentationReader.read();
        List<LandKundeAuftragCollectionVo> landKundeAuftragCollectionVos = segmentationProcessor.process(landKundenDtos);
        segmentationWriter.write(Arrays.asList(landKundeAuftragCollectionVos));
        return RepeatStatus.FINISHED;
    }
}
