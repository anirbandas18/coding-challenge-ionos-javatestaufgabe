package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.upload;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.FileBucketDto;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
public class UploadProcessor implements ItemProcessor<List<FileBucketDto>, List<FileBucketDto>> {

    private MinioClient minioClient;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Create bucket for each country to upload csv files to iff the bucket for a country doesn't exist
     * @param item
     * @throws MinioException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Override
    public List<FileBucketDto> process(List<FileBucketDto> item) throws Exception {
        int count = 0;
        for(FileBucketDto fileBucketDto : item) {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(fileBucketDto.getBucketName()).build());
            log.debug("Bucket: {} already exists: {}", fileBucketDto.getBucketName(), found);
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(fileBucketDto.getBucketName()).build());
                log.info("Created bucket: {} for upload", fileBucketDto.getBucketName());
                count++;
            }
        }
        log.info("Created {} buckets", count);
        return item;
    }

}
