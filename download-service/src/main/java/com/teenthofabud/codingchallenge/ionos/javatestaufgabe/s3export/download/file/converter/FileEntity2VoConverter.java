package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileVo;
import com.teenthofabud.core.common.error.TOABSystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class FileEntity2VoConverter implements Converter<FileEntity, FileVo> {

    private String bucketNameDelimitter;
    //private String bucketTimestampFormat;

    private Tika tika;

    @Autowired
    public void setTika(Tika tika) {
        this.tika = tika;
    }

    /*@Value("${s3export.download.job.bucket.timestamp.format:yyyy-MM-dd}")
    public void setBucketTimestampFormat(String bucketTimestampFormat) {
        this.bucketTimestampFormat = bucketTimestampFormat;
    }*/

    @Value("${s3export.download.delimitter.bucket.name:_}")
    public void setBucketNameDelimitter(String bucketNameDelimitter) {
        this.bucketNameDelimitter = bucketNameDelimitter;
    }

    @Override
    public FileVo convert(FileEntity source) {
        FileVo target = new FileVo();
        target.setName(source.getName());
        try {
            target.setContent(source.getFilePointer().readAllBytes());
        } catch (IOException e) {
            log.error("unable to read file: {}", e);
            throw new TOABSystemException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE,
                    "unable to read file : " + e.getMessage(), new Object[] { "name: " + source.getName() });
        }
        InputStream is = new ByteArrayInputStream(target.getContent());
        try {
            String mimeType = tika.detect(is);
            target.setType(mimeType);
        } catch (IOException e) {
            log.error("unable to detect media type of file: {}", e);
            throw new TOABSystemException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE,
                    "unable to detect mime type of file : " + e.getMessage(), new Object[] { "name: " + source.getName() });
        }
        log.debug("Converted {} to {}", source, target);
        return target;
    }

}
