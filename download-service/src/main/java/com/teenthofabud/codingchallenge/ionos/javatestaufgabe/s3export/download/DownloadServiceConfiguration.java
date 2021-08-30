package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DownloadServiceConfiguration {

    private String bucketBaseUrl;
    private String bucketAccessKey;
    private String bucketSecretKey;

    @Value("${s3export.download.bucket.base.url:https://play.min.io}")
    public void setBucketBaseUrl(String bucketBaseUrl) {
        this.bucketBaseUrl = bucketBaseUrl;
    }

    @Value("${s3export.download.bucket.access.key:Q3AM3UQ867SPQQA43P2F}")
    public void setBucketAccessKey(String bucketAccessKey) {
        this.bucketAccessKey = bucketAccessKey;
    }

    @Value("${s3export.download.bucket.secret.key:zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG}")
    public void setBucketSecretKey(String bucketSecretKey) {
        this.bucketSecretKey = bucketSecretKey;
    }


    @Bean
    public MinioClient minioClient() {
        log.info("creating minio client with url: {}", bucketBaseUrl);
        MinioClient minioClient = MinioClient.builder()
                .endpoint(bucketBaseUrl).credentials(bucketAccessKey, bucketSecretKey)
                .build();
        return minioClient;
    }

    @Bean
    public Tika tike() {
        return new Tika();
    }

}
