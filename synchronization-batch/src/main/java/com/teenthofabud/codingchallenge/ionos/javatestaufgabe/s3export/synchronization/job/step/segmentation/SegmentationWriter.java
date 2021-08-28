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

@Slf4j
public class SegmentationWriter implements ItemWriter<List<LandKundeAuftragCollectionVo>> {

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

    @Value("${s3export.sync.timezone:Europe/Paris}")
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

    @BeforeWrite
    public void beforeWrite(List<? extends LandKundeAuftragCollectionVo> items) throws
            MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        String systemBaseLocation = System.getProperty("user.dir");
        storageAbsolutePath = Paths.get(systemBaseLocation, storeBaseLocation);
        fileNameFileLocationMap = new TreeMap<>();
        int count = 0;
        for(LandKundeAuftragCollectionVo csvFileDetails : items) {
            String country = csvFileDetails.getLand();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(country).build());
            log.info("Bucket: {} exists: {}", country, found);
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(country).build());
                log.info("Created bucket: {} for upload", country);
                count++;
            }
        }
        log.info("Created {} buckets", count);
    }

    @AfterWrite
    public void afterWrite(List<? extends LandKundeAuftragCollectionVo> items) throws
            MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        ZonedDateTime retentionTimestamp = getRetentionTimestamp();
        Retention retention = new Retention(RetentionMode.COMPLIANCE, retentionTimestamp);
        Map<String,Integer> uploadMetrics = new TreeMap<>();
        for(LandKundeAuftragCollectionVo csvFileDetails : items) {
            String country = csvFileDetails.getLand();
            String timestamp = csvFileDetails.getDate();
            String csvFileName = getFileName(country, timestamp);
            if(fileNameFileLocationMap.containsKey(csvFileName)) {
                String fileLocation = fileNameFileLocationMap.get(csvFileName);
                log.info("File: {} exists at location: {}", fileLocation);
                ObjectWriteResponse response = minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(country)
                                .object(csvFileName)
                                .contentType(MediaType.CSV_UTF_8.type())
                                //.region("")
                                .retention(retention)
                                .filename(fileLocation)
                                .build());
                log.info("Uploaded file: {} to bucket: {}", csvFileName, country);
                int count = uploadMetrics.containsKey(country) ? uploadMetrics.get(country) : 0;
                uploadMetrics.put(country, ++count);
            }
        };
        for(String country : uploadMetrics.keySet()) {
            Integer count = uploadMetrics.get(country);
            log.info("Uploaded {} files to bucket: {}", count, country);
        }
    }

    /**
     * Convert land kunde ayftrag mapping to csv line item
     * Form csv file name
     * Save land kunde auftrag map that is already grouped by map before coming into this flow as a csv file on file system
     * After completing the write, upload all locally saved files to bucket via file name mapping on the basis of country against country based bucket name
     * @param items
     * @throws Exception
     */
    @Override
    public void write(List<? extends List<LandKundeAuftragCollectionVo>> items) throws Exception {
        if(items.isEmpty()) {
            // abort job because no data to write
            log.error("");
        }
        List<LandKundeAuftragCollectionVo> landkundeAuftraVoList = items.get(0);
        for(LandKundeAuftragCollectionVo csvFileDetails : landkundeAuftraVoList) {
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
            log.info("Saved file: {} to {}", csvFileName, csvFilePath);
        }
        log.info("Saved {} files in total", fileNameFileLocationMap.size());
    }
}
