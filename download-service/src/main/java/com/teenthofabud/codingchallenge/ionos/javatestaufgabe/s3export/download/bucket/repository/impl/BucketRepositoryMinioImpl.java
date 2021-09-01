package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.repository.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.converter.BucketRawToEntityConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.repository.BucketRepository;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BucketRepositoryMinioImpl implements BucketRepository {

    private static final Comparator<BucketEntity> CMP_BY_COUNTRY_DATE = (s1, s2) -> {
        return Integer.compare(s1.getCountry().compareTo(s2.getCountry()),
                s1.getDate().compareTo(s2.getDate()));
    };

    private MinioClient minioClient;
    private BucketRawToEntityConverter rawToEntityConverter;

    private String bucketNameRegexDateTemplate;

    @Value("${s3export.download.job.bucket.name.regex.date.template}")
    public void setBucketNameRegexDateTemplate(String bucketNameRegexDateTemplate) {
        this.bucketNameRegexDateTemplate = bucketNameRegexDateTemplate;
    }

    @Autowired
    public void setRawToEntityConverter(BucketRawToEntityConverter rawToEntityConverter) {
        this.rawToEntityConverter = rawToEntityConverter;
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public List<BucketEntity> findAll() throws BucketException {
        List<BucketEntity> bucketEntities = new LinkedList<>();
        try {
            List<Bucket> bucketList = minioClient.listBuckets();
            List<Bucket> filteredBuckets = bucketList.stream()
                    .filter(b -> b.name().matches(bucketNameRegexDateTemplate)).collect(Collectors.toList());
            bucketEntities = filteredBuckets.stream().map(b -> rawToEntityConverter.convert(b)).collect(Collectors.toList());
            Collections.sort(bucketEntities, CMP_BY_COUNTRY_DATE);
            log.info("Retrieved {} buckets for the service", bucketEntities.size());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Error getting all available buckets: {}", e);
            throw new BucketException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE,
                    "Unable to get available buckets", new Object[] {e.getMessage()});
        }
        return bucketEntities;
    }

    @Override
    public Optional<BucketEntity> findByName(String name) throws BucketException {
        Optional<BucketEntity> bucketEntity = Optional.empty();
        try {
            List<Bucket> bucketList = minioClient.listBuckets();
            Optional<Bucket> bucket = bucketList.stream().filter(b -> b.name().equalsIgnoreCase(name)).findFirst();
            if(bucket.isEmpty()) {
                log.error("No bucket found matching name {}", name);
                throw new BucketException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "name: " + name });
            }
            bucketEntity = Optional.of(rawToEntityConverter.convert(bucket.get()));
            log.info("Retrieved bucket with name {}", bucket.get().name());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Error getting all available buckets: {}", e);
            throw new BucketException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE,
                    "Unable to get available buckets", new Object[] {e.getMessage()});
        }
        return bucketEntity;
    }

}