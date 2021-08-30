package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import com.teenthofabud.core.common.error.TOABSystemException;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class BucketRawToEntityConverter implements Converter<Bucket, BucketEntity>, InitializingBean {

    private static final int NUMBER_OF_TOKENS = 3;

    private String bucketNameDelimitter;
    private String bucketTimestampFormat;
    private String timezone;
    private SimpleDateFormat sdf;
    private DateTimeFormatter dtf;

    @Value("${s3export.download.timezone:Europe/Paris}")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Value("${s3export.download.job.bucket.timestamp.format:yyyy-MM-dd}")
    public void setBucketTimestampFormat(String bucketTimestampFormat) {
        this.bucketTimestampFormat = bucketTimestampFormat;
    }

    @Value("${s3export.download.delimitter.bucket.name:-}")
    public void setBucketNameDelimitter(String bucketNameDelimitter) {
        this.bucketNameDelimitter = bucketNameDelimitter;
    }

    @Override
    public BucketEntity convert(Bucket source) {
        BucketEntity entity = new BucketEntity();
        String tkns[] = source.name().split("\\" + bucketNameDelimitter);
        if(tkns == null || tkns.length != NUMBER_OF_TOKENS) {
            throw new TOABSystemException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE, "Invalid bucket name format: " + source.name());
        }
        entity.setCountry(tkns[1]);
        LocalDate localDate = LocalDate.parse(tkns[2], dtf);
        entity.setDate(localDate);
        entity.setPrefix(tkns[0]);
        entity.setName(source.name());
        return entity;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sdf = new SimpleDateFormat(bucketTimestampFormat);
        dtf = DateTimeFormatter.ofPattern(bucketTimestampFormat);
    }
}
