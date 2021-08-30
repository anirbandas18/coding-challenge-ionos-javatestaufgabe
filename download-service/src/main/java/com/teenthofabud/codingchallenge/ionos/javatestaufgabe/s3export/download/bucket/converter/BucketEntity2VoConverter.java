package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketVo;
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
public class BucketEntity2VoConverter implements Converter<BucketEntity, BucketVo>, InitializingBean {

    private String bucketNameDelimitter;
    private String bucketTimestampFormat;
    private DateTimeFormatter dtf;

    @Value("${s3export.download.job.bucket.timestamp.format:yyyy-MM-dd}")
    public void setBucketTimestampFormat(String bucketTimestampFormat) {
        this.bucketTimestampFormat = bucketTimestampFormat;
    }

    @Value("${s3export.download.delimitter.bucket.name:_}")
    public void setBucketNameDelimitter(String bucketNameDelimitter) {
        this.bucketNameDelimitter = bucketNameDelimitter;
    }

    @Override
    public BucketVo convert(BucketEntity source) {
        BucketVo target = new BucketVo();
        String bucketTimestamp = source.getDate().format(dtf);
        String bucketName = String.join(bucketNameDelimitter, source.getPrefix(), source.getCountry(), bucketTimestamp);
        target.setName(bucketName);
        log.debug("Converted {} to {}", source, target);
        return target;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dtf = DateTimeFormatter.ofPattern(bucketTimestampFormat);
    }
}
