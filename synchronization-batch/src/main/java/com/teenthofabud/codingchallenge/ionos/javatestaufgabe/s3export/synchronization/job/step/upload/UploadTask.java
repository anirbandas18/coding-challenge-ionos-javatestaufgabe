package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.upload;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.FileBucketDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class UploadTask implements Tasklet {

    private UploadReader reader;
    private UploadProcessor processor;
    private UploadWriter writer;

    @Autowired
    public void setReader(UploadReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(UploadProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(UploadWriter writer) {
        this.writer = writer;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<FileBucketDto> fileBucketMap = reader.read();
        fileBucketMap = processor.process(fileBucketMap);
        writer.write(Arrays.asList(fileBucketMap));
        return RepeatStatus.FINISHED;
    }
}
