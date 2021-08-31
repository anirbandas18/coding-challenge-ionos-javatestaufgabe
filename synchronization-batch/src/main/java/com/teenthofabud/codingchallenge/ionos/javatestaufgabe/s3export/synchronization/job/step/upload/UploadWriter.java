package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.upload;

import com.google.common.net.MediaType;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.FileBucketDto;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class UploadWriter implements ItemWriter<List<FileBucketDto>> {

    private MinioClient minioClient;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
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
        Map<String,Integer> uploadMetrics = new TreeMap<>();
        for(FileBucketDto fileBucketDto : fileBucketDtoList) {
            Path filePath = Paths.get(fileBucketDto.getFileLocation());
            if(Files.exists(filePath)) {
                log.debug("File: {} exists at its concerned location", filePath.toString());
                ObjectWriteResponse response = minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(fileBucketDto.getBucketName())
                                .object(fileBucketDto.getFileName())
                                .contentType(MediaType.CSV_UTF_8.type())
                                .filename(filePath.toString())
                                .build());
                log.info("Uploaded file: {} to bucket: {} from: {}", fileBucketDto.getFileName(), fileBucketDto.getBucketName(), filePath.toString());
                Files.delete(filePath);
                log.debug("Deleted file {} post upload", filePath.toString());
                int count = uploadMetrics.containsKey(fileBucketDto.getBucketName()) ? uploadMetrics.get(fileBucketDto.getBucketName()) : 0;
                uploadMetrics.put(fileBucketDto.getBucketName(), ++count);
            } else {
                log.debug("File: {} does not exist at its concerned location", filePath.toString());
            }
        };
        for(String country : uploadMetrics.keySet()) {
            Integer count = uploadMetrics.get(country);
            log.debug("Uploaded {} files to bucket: {}", count, country);
        }
    }
}
