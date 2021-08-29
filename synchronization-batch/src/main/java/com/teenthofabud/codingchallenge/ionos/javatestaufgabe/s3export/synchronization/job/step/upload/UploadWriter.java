package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.upload;

import com.google.common.net.MediaType;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.FileBucketDto;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import io.minio.messages.Retention;
import io.minio.messages.RetentionMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class UploadWriter implements ItemWriter<List<FileBucketDto>> {

    private MinioClient minioClient;
    private String timeZone;
    private Long retentionPeriodInYears;

    @Value("${s3export.sync.timezone:Europe/Paris}")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Value("${s3export.sync.retention.in.years:3}")
    public void setRetentionPeriodInYears(Long retentionPeriodInYears) {
        this.retentionPeriodInYears = retentionPeriodInYears;
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    private ZonedDateTime getRetentionTimestamp() {
        ZoneId ect = ZoneId.of(timeZone);
        LocalDateTime localTimestampNow = LocalDateTime.now();
        ZonedDateTime dateAndTimeInEct = ZonedDateTime.of(localTimestampNow, ect);
        ZonedDateTime dateAndTimeNowInUtc = dateAndTimeInEct.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime dateAndTimeForFutureInUtc = dateAndTimeNowInUtc.plusYears(retentionPeriodInYears);
        return dateAndTimeForFutureInUtc;
    }

    /**
     * Upload csv file for each country based on the current job instance's creation time to the country specific bucket for the day
     * @param items
     * @throws MinioException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Override
    public void write(List<? extends List<FileBucketDto>> items) throws Exception {
        List<FileBucketDto> fileBucketDtoList = items.get(0);
        ZonedDateTime retentionTimestamp = getRetentionTimestamp();
        Retention retention = new Retention(RetentionMode.COMPLIANCE, retentionTimestamp);
        Map<String,Integer> uploadMetrics = new TreeMap<>();
        for(FileBucketDto fileBucketDto : fileBucketDtoList) {
            String fileLocation = fileBucketDto.getFileLocation();
            log.info("File: {} exists at location: {}", fileLocation);
            ObjectWriteResponse response = minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(fileBucketDto.getBucketName())
                            .object(fileBucketDto.getFileName())
                            .contentType(MediaType.CSV_UTF_8.type())
                            //.region("")
                            .retention(retention)
                            .filename(fileLocation)
                            .build());
            log.info("Uploaded file: {} to bucket: {}", fileBucketDto.getFileName(), fileBucketDto.getBucketName());
            int count = uploadMetrics.containsKey(fileBucketDto.getBucketName()) ? uploadMetrics.get(fileBucketDto.getBucketName()) : 0;
            uploadMetrics.put(fileBucketDto.getBucketName(), ++count);
        };
        for(String country : uploadMetrics.keySet()) {
            Integer count = uploadMetrics.get(country);
            log.info("Uploaded {} files to bucket: {}", count, country);
        }
    }
}
