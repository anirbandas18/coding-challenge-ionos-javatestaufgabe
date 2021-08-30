package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.FileBucketCollectionDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.FileBucketDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.KundeAuftragVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.LandKundeAuftragCollectionVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.redis.FileNameLocationCollectionRepository;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class SegmentationWriter implements ItemWriter<List<LandKundeAuftragCollectionVo>>, InitializingBean {

    private static final String FILE_EXTENSION_DELIMITTER = ".";

    private List<FileBucketDto> fileNameLocationMap;
    private String fileNameDelimitter;
    private String fileExtension;
    private MinioClient minioClient;
    private String storeBaseLocation;
    private String fileTimestampFormat;
    private String bucketTimestampFormat;
    private SimpleDateFormat fileSdf;
    private SimpleDateFormat bucketSdf;
    private FileNameLocationCollectionRepository repository;
    private StepExecution stepExecution;
    private String fileBucketCollectionKeyName;
    private String keyNameDelimitter;
    private String jobParameterName1;
    private String bucketNamePrefix;
    private String bucketNameDelimitter;

    @Value("${s3export.sync.delimitter.bucket.name:-}")
    public void setBucketNameDelimitter(String bucketNameDelimitter) {
        this.bucketNameDelimitter = bucketNameDelimitter;
    }

    @Value("${s3export.sync.job.bucket.name.prefix}")
    public void setBucketNamePrefix(String bucketNamePrefix) {
        this.bucketNamePrefix = bucketNamePrefix;
    }

    @Value("${s3export.sync.filebucketcollection.key.name:fileBucketCollectionKey}")
    public void setFileBucketCollectionKeyName(String fileBucketCollectionKeyName) {
        this.fileBucketCollectionKeyName = fileBucketCollectionKeyName;
    }

    @Value("${s3export.sync.job.bucket.timestamp.format:YYYY-MM-dd}")
    public void setBucketTimestampFormat(String bucketTimestampFormat) {
        this.bucketTimestampFormat = bucketTimestampFormat;
    }

    @Value("${s3export.sync.store.base.path}")
    public void setStoreBaseLocation(String storeBaseLocation) {
        this.storeBaseLocation = storeBaseLocation;
    }

    @Value("${s3export.sync.job.file.timestamp.format:YYYY-MM-dd_HH-mm-ss}")
    public void setFileTimestampFormat(String fileTimestampFormat) {
        this.fileTimestampFormat = fileTimestampFormat;
    }

    @Value("${s3export.sync.delimitter.file.name:+}")
    public void setFileNameDelimitter(String fileNameDelimitter) {
        this.fileNameDelimitter = fileNameDelimitter;
    }

    @Value("${s3export.sync.export.file.extension:csv}")
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Value("${s3export.sync.delimitter.key.name:~}")
    public void setKeyNameDelimitter(String keyNameDelimitter) {
        this.keyNameDelimitter = keyNameDelimitter;
    }

    @Value("${s3export.sync.job.parameter.1:synchronization-timestamp}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Autowired
    public void setRepository(FileNameLocationCollectionRepository repository) {
        this.repository = repository;
    }

    @BeforeStep
    public void beforeStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @AfterStep
    public void afterStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    private String getFileName(String country, Long timestamp) {
        Date synchronizationDate = new Date();
        synchronizationDate.setTime(timestamp);
        String synchronizationDateStr = fileSdf.format(synchronizationDate);
        StringBuffer sbf = new StringBuffer();
        sbf.append(country);
        sbf.append(fileNameDelimitter);
        sbf.append(synchronizationDateStr);
        sbf.append(FILE_EXTENSION_DELIMITTER);
        sbf.append(fileExtension);
        return sbf.toString();
    }

    private String getBucketName(String country, Long timestamp) {
        Date dt = new Date();
        dt.setTime(timestamp);
        String date = bucketSdf.format(dt);
        String bucketName = String.join(bucketNameDelimitter, bucketNamePrefix, country, date);
        return bucketName;
    }

    private String getFileBucketCollectionKeyValue() {
        String stepName = stepExecution.getStepName();
        JobExecution jobExecution = stepExecution.getJobExecution();
        String jobId = String.valueOf(jobExecution.getJobId());
        String jobInstanceName = jobExecution.getJobInstance().getJobName();
        String jobInstanceId = String.valueOf(jobExecution.getJobInstance().getInstanceId());
        String jobExecutionId = String.valueOf(jobExecution.getId());
        JobParameters jobParameters = stepExecution.getJobParameters();
        Map<String, JobParameter> parametersMap = jobParameters.getParameters();
        JobParameter parameter = parametersMap.get(jobParameterName1);
        String jobParameterValue1 = parameter.getValue().toString();
        String keyValue = String.join(keyNameDelimitter, Arrays.asList(jobId, jobInstanceName, jobInstanceId, jobExecutionId, jobParameterValue1, stepName));
        return keyValue;
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
        List<LandKundeAuftragCollectionVo> landKundeAuftragVoList = items.get(0);
        for(LandKundeAuftragCollectionVo csvFileDetails : landKundeAuftragVoList) {
            String bucketName = getBucketName(csvFileDetails.getLand(), csvFileDetails.getTimestamp());
            String csvFileName = getFileName(csvFileDetails.getLand(), csvFileDetails.getTimestamp());
            Path csvFilePath = Paths.get(storeBaseLocation.toString(), csvFileDetails.getLand(), csvFileName);
            Writer writer  = new StringWriter();
            StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();
            List<KundeAuftragVo> lineItem = csvFileDetails.getItems();
            sbc.write(lineItem);
            FileBucketDto fileNameLocation = new FileBucketDto();
            fileNameLocation.setFileName(csvFileName);
            fileNameLocation.setBucketName(bucketName);
            fileNameLocation.setFileLocation(csvFilePath.toString());
            fileNameLocationMap.add(fileNameLocation);
            String csvFileContent = writer.toString();
            Files.createDirectories(csvFilePath.getParent());
            Files.write(csvFilePath, csvFileContent.getBytes(StandardCharsets.UTF_8));
            writer.close();
            log.debug("Saved file {}", csvFilePath);
        }
        destroy();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        fileSdf = new SimpleDateFormat(fileTimestampFormat);
        bucketSdf = new SimpleDateFormat(bucketTimestampFormat);
        fileNameLocationMap = new LinkedList<>();
    }

    public void destroy() throws Exception {
        String keyValue = getFileBucketCollectionKeyValue();
        log.info("Passing down file name location collection key value: {} for next step to access it via key name: {} and use it for further processing",
                keyValue, fileBucketCollectionKeyName);
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put(fileBucketCollectionKeyName, keyValue);
        FileBucketCollectionDto fileBucketCollectionDto = new FileBucketCollectionDto();
        fileBucketCollectionDto.setCollectionKey(keyValue);
        fileBucketCollectionDto.setFileNameLocationMap(fileNameLocationMap);
        repository.save(fileBucketCollectionDto);
        log.info("Saved {} files in total", fileNameLocationMap.size());
    }
}
