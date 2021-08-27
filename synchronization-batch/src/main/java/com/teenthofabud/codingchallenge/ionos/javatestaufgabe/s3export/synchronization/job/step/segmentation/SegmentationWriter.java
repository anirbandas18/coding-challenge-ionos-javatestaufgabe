package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation;

import com.google.common.net.MediaType;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.KundeAuftragVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.LandKundeAuftragCollectionVo;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Retention;
import io.minio.messages.RetentionMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
@Slf4j
@StepScope
public class SegmentationWriter implements ItemWriter<LandKundeAuftragCollectionVo> {

    private static final String FILE_EXTENSION_DELIMITTER = ".";

    private Map<String, String> fileNameFileLocationMap;
    private Long retentionPeriodInYears;
    private String fileNameDelimitter;
    private String fileExtension;
    private MinioClient minioClient;
    private String timeZone;
    private String storeBaseLocation;
    private Path storageAbsolutePath;

    @Value("${s3export.sync.store.base.path:/ionos-javatestaufgabe-s3export/s3export-synchronization-batch}")
    public void setStoreBaseLocation(String storeBaseLocation) {
        this.storeBaseLocation = storeBaseLocation;
    }

    @Value("${s3export.sync.timezone:ECT}")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Value("${s3export.sync.retention.in.years:3}")
    public void setRetentionPeriodInYears(Long retentionPeriodInYears) {
        this.retentionPeriodInYears = retentionPeriodInYears;
    }

    @Value("${s3export.sync.delimitter.file.name:+}")
    public void setFileNameDelimitter(String fileNameDelimitter) {
        this.fileNameDelimitter = fileNameDelimitter;
    }

    @Value("${s3export.sync.export.file.extension:csv}")
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    private String getFileName(String country, String timestamp) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(country);
        sbf.append(fileNameDelimitter);
        sbf.append(timestamp);
        sbf.append(FILE_EXTENSION_DELIMITTER);
        sbf.append(fileExtension);
        return sbf.toString();
    }

    private ZonedDateTime getRetentionTimestamp() {
        ZoneId ect = ZoneId.of(timeZone);
        LocalDateTime localTimestampNow = LocalDateTime.now();
        ZonedDateTime dateAndTimeInEct = ZonedDateTime.of(localTimestampNow, ect);
        ZonedDateTime dateAndTimeNowInUtc = dateAndTimeInEct.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime dateAndTimeForFutureInUtc = dateAndTimeNowInUtc.plusYears(retentionPeriodInYears);
        return dateAndTimeForFutureInUtc;
    }

    @Override
    public void write(List<? extends LandKundeAuftragCollectionVo> items) throws Exception {
        for(LandKundeAuftragCollectionVo csvFileDetails : items) {
            String country = csvFileDetails.getLand();
            String timestamp = csvFileDetails.getDate();
            String csvFileName = getFileName(country, timestamp);
            Path csvFilePath = Paths.get(storageAbsolutePath.toString(), csvFileName);
            Writer writer  = new FileWriter(csvFilePath.toFile());
            StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();
            List<KundeAuftragVo> lineItem = csvFileDetails.getItems();
            sbc.write(lineItem);
            fileNameFileLocationMap.put(csvFileName, csvFilePath.toString());
            writer.close();
        }
    }

    @BeforeWrite
    public void beforeWrite(List<? extends LandKundeAuftragCollectionVo> items) throws
            MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        String systemBaseLocation = System.getProperty("user.dir");
        storageAbsolutePath = Paths.get(systemBaseLocation, storeBaseLocation);
        fileNameFileLocationMap = new TreeMap<>();
        for(LandKundeAuftragCollectionVo csvFileDetails : items) {
            String country = csvFileDetails.getLand();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(country).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(country).build());
            }
        }
    }

    @AfterWrite
    public void afterWrite(List<? extends LandKundeAuftragCollectionVo> items) throws
            MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        ZonedDateTime retentionTimestamp = getRetentionTimestamp();
        Retention retention = new Retention(RetentionMode.COMPLIANCE, retentionTimestamp);
        for(LandKundeAuftragCollectionVo csvFileDetails : items) {
            String country = csvFileDetails.getLand();
            String timestamp = csvFileDetails.getDate();
            String csvFileName = getFileName(country, timestamp);
            if(fileNameFileLocationMap.containsKey(csvFileName)) {
                String fileLocation = fileNameFileLocationMap.get(csvFileName);
                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(country)
                                .object(csvFileName)
                                .contentType(MediaType.CSV_UTF_8.type())
                                //.region("")
                                .retention(retention)
                                .filename(fileLocation)
                                .build());
            }
        }
    }

}
