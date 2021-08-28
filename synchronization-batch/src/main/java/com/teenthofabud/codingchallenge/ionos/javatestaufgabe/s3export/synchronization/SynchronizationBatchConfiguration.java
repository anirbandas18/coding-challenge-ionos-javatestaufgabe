package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.LandKundenDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.LandKundeAuftragCollectionVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.listener.TOABItemFailureListener;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.listener.TOABNoWorkFoundStepExecutionListener;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering.FilteringProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering.FilteringReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering.FilteringWriter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping.MappingProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping.MappingReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping.MappingWriter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation.SegmentationProcessor;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation.SegmentationReader;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation.SegmentationWriter;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@EnableBatchProcessing
@EnableRedisRepositories
@EnableScheduling
@EnableJpaRepositories
@Slf4j
public class SynchronizationBatchConfiguration implements InitializingBean {

    private String auftragKundeCollectionKeyName;
    private String kundeAuftragCollectionKeyName;
    private Integer batchSize;
    private String bucketBaseUrl;
    private String bucketAccessKey;
    private String bucketSecretKey;
    private String storeBaseLocation;
    private String appName;
    private StepBuilderFactory stepBuilderFactory;
    private JobBuilderFactory jobBuilderFactory;

    @Value("${s3export.sync.store.base.path:/ionos-javatestaufgabe-s3export/s3export-synchronization-batch}")
    public void setStoreBaseLocation(String storeBaseLocation) {
        this.storeBaseLocation = storeBaseLocation;
    }

    @Value("${s3export.sync.bucket.base.url:https://play.min.io}")
    public void setBucketBaseUrl(String bucketBaseUrl) {
        this.bucketBaseUrl = bucketBaseUrl;
    }

    @Value("${s3export.sync.bucket.access.key:Q3AM3UQ867SPQQA43P2F}")
    public void setBucketAccessKey(String bucketAccessKey) {
        this.bucketAccessKey = bucketAccessKey;
    }

    @Value("${s3export.sync.bucket.secret.key:zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG}")
    public void setBucketSecretKey(String bucketSecretKey) {
        this.bucketSecretKey = bucketSecretKey;
    }

    @Value("${s3export.sync.batch.size:1000}")
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    @Value("${s3export.sync.auftragkundecollection.key.name:auftragKundeCollectionKey}")
    public void setAuftragKundeCollectionKeyName(String auftragKundeCollectionKeyName) {
        this.auftragKundeCollectionKeyName = auftragKundeCollectionKeyName;
    }

    @Value("${s3export.sync.kundeauftragcollection.key.name:kundeAuftragCollectionKey}")
    public void setKundeAuftragCollectionKeyName(String kundeAuftragCollectionKeyName) {
        this.kundeAuftragCollectionKeyName = kundeAuftragCollectionKeyName;
    }

    @Value("${spring.application.name:synchronization-job}")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    /**
     * Promote data from execution context of one step to another
     * @return
     */
    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[] { auftragKundeCollectionKeyName, kundeAuftragCollectionKeyName });
        return listener;
    }

    @Bean
    public MinioClient minioClient() {
        log.info("creating minio client with url: {}", bucketBaseUrl);
        MinioClient minioClient = MinioClient.builder()
                        .endpoint(bucketBaseUrl).credentials(bucketAccessKey, bucketSecretKey)
                        .build();
        return minioClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String systemBaseLocation = System.getProperty("user.dir");
        Path storageAbsolutePath = Paths.get(systemBaseLocation, storeBaseLocation);
        Files.createDirectories(storageAbsolutePath.getParent());
        log.info("Creating all directories if not present within the absolute path: {}", storageAbsolutePath);
    }

    @Bean
    @StepScope
    public FilteringReader filteringReader() {
        return new FilteringReader();
    }

    @Bean
    @StepScope
    public FilteringProcessor filteringProcessor() {
        return new FilteringProcessor();
    }

    @Bean
    @StepScope
    public FilteringWriter filteringWriter() {
        return new FilteringWriter();
    }

    @Bean
    public Step filtering() {
        return stepBuilderFactory.get("filtering")
                .<List<AuftraegeEntity>, List<AuftragKundeDto>>chunk(batchSize)
                .reader(filteringReader())
                .processor(filteringProcessor())
                .writer(filteringWriter())
                .listener(new TOABNoWorkFoundStepExecutionListener())
                .listener(new TOABItemFailureListener<List<AuftragKundeDto>, List<KundeAuftragDto>>())
                .build();
    }

    @Bean
    @StepScope
    public MappingReader mappingReader() {
        return new MappingReader();
    }

    @Bean
    @StepScope
    public MappingProcessor mappingProcessor() {
        return new MappingProcessor();
    }

    @Bean
    @StepScope
    public MappingWriter mappingWriter() {
        return new MappingWriter();
    }

    @Bean
    public Step mapping() {
        return stepBuilderFactory.get("mapping")
                .<List<AuftragKundeDto>, List<KundeAuftragDto>>chunk(batchSize)
                .reader(new MappingReader())
                .processor(new MappingProcessor())
                .writer(new MappingWriter())
                .listener(new TOABNoWorkFoundStepExecutionListener())
                .listener(new TOABItemFailureListener<List<AuftragKundeDto>, List<KundeAuftragDto>>())
                .build();
    }

    @Bean
    @StepScope
    public SegmentationReader segmentationReader() {
        return new SegmentationReader();
    }

    @Bean
    @StepScope
    public SegmentationProcessor segmentationProcessor() {
        return new SegmentationProcessor();
    }

    @Bean
    @StepScope
    public SegmentationWriter segmentationWriter() {
        return new SegmentationWriter();
    }

    @Bean
    public Step segmentation() {
        return stepBuilderFactory.get("segmentation")
                .<List<LandKundenDto>, List<LandKundeAuftragCollectionVo>>chunk(batchSize)
                .reader(segmentationReader())
                .processor(segmentationProcessor())
                .writer(segmentationWriter())
                .listener(new TOABNoWorkFoundStepExecutionListener())
                .listener(new TOABItemFailureListener<List<AuftragKundeDto>, List<KundeAuftragDto>>())
                .build();
    }

    @Bean
    public Job synchronizationJob() {
        return jobBuilderFactory.get(appName)
                .start(filtering())
                .next(mapping())
                .next(segmentation())
                .build();
    }

}