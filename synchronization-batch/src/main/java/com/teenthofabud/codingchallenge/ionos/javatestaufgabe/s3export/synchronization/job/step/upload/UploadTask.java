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

    private UploadReader uploadReader;
    private UploadProcessor uploadProcessor;
    private UploadWriter uploadWriter;

    @Autowired
    public void setUploadReader(UploadReader uploadReader) {
        this.uploadReader = uploadReader;
    }

    @Autowired
    public void setUploadProcessor(UploadProcessor uploadProcessor) {
        this.uploadProcessor = uploadProcessor;
    }

    @Autowired
    public void setUploadWriter(UploadWriter uploadWriter) {
        this.uploadWriter = uploadWriter;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<FileBucketDto> fileBucketMap = uploadReader.read();
        fileBucketMap = uploadProcessor.process(fileBucketMap);
        uploadWriter.write(Arrays.asList(fileBucketMap));
        return RepeatStatus.FINISHED;
    }
}
