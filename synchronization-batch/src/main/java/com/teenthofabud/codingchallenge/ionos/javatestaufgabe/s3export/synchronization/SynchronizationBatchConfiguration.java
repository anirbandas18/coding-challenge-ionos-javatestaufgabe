package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
@EnableRedisRepositories
@Slf4j
public class SynchronizationBatchConfiguration implements InitializingBean {

    private String auftragKundeCollectionKeyName;
    private String kundeAuftragCollectionKeyName;
    private Long batchSize;
    private String bucketBaseUrl;
    private String bucketAccessKey;
    private String bucketSecretKey;
    private String storeBaseLocation;

    @Value("${s3export.sync.store.base.path:/ionos-javatestaufgabe-s3export/s3export-synchronization-batch}")
    public void setStoreBaseLocation(String storeBaseLocation) {
        this.storeBaseLocation = storeBaseLocation;
    }

    @Value("{s3export.sync.bucket.base.url:https://play.min.io}")
    public void setBucketBaseUrl(String bucketBaseUrl) {
        this.bucketBaseUrl = bucketBaseUrl;
    }

    @Value("{s3export.sync.bucket.access.key:Q3AM3UQ867SPQQA43P2F}")
    public void setBucketAccessKey(String bucketAccessKey) {
        this.bucketAccessKey = bucketAccessKey;
    }

    @Value("{s3export.sync.bucket.secret.key:zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG}")
    public void setBucketSecretKey(String bucketSecretKey) {
        this.bucketSecretKey = bucketSecretKey;
    }

    @Value("${s3export.sync.batch.size:100}")
    public void setBatchSize(Long batchSize) {
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

    private Long getChunkSize() {
        int noOfCores = Runtime.getRuntime().availableProcessors();
        return batchSize / noOfCores;
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
        Files.createFile(storageAbsolutePath);
        log.info("Creating all directories if not present within the absolute path: {}", storageAbsolutePath);
    }
}
