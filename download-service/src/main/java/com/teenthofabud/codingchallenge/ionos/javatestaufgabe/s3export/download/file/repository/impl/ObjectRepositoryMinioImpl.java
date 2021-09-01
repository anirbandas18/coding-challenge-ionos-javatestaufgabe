package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.repository.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileDetailEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.repository.FileRepository;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class ObjectRepositoryMinioImpl implements FileRepository, InitializingBean {

    private static final String FILE_EXTENSION_DELIMITTER = ".";
    private static final Comparator<FileDetailEntity> CMP_BY_COUNTRY_ASC_DATE_DESC = (s1, s2) -> {
        return Integer.compare(s1.getCountry().compareTo(s2.getCountry()),
                s1.getDateTime().compareTo(s2.getDateTime()));
    };

    private DateTimeFormatter fileDtf;

    private String fileTimestampFormat;
    private String fileNameDelimitter;

    @Value("${s3export.download.job.file.timestamp.format:yyyy-MM-dd'_'HH-mm-ss}")
    public void setFileTimestampFormat(String fileTimestampFormat) {
        this.fileTimestampFormat = fileTimestampFormat;
    }

    @Value("${s3export.download.delimitter.file.name:+}")
    public void setFileNameDelimitter(String fileNameDelimitter) {
        this.fileNameDelimitter = fileNameDelimitter;
    }

    private MinioClient minioClient;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    private FileDetailEntity parseObjectName(String objectName) throws FileException, ParseException {
        String tkns[] = objectName.split("\\" + FILE_EXTENSION_DELIMITTER);
        if(tkns == null || tkns.length == 0) {
            throw new FileException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE,
                    "invalid object name with extension", new Object[] { "object name", objectName });
        }

        tkns = tkns[0].split("\\" + fileNameDelimitter);
        LocalDateTime localDateTime = LocalDateTime.parse(tkns[1], fileDtf);
        FileDetailEntity fileDetailEntity = new FileDetailEntity();
        fileDetailEntity.setName(objectName);
        fileDetailEntity.setCountry(tkns[0]);
        fileDetailEntity.setDateTime(localDateTime);
        return fileDetailEntity;
    }

    @Override
    public List<FileDetailEntity> findAllReferencesBy(String bucketName) throws FileException {
        List<FileDetailEntity> fileDetailEntities = new LinkedList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());
        if(results != null) {
            for(Result<Item> result : results) {
                try {
                    Item item = result.get();
                    FileDetailEntity fileDetailEntity = parseObjectName(item.objectName());
                    fileDetailEntities.add(fileDetailEntity);
                    log.debug("Retrieved object with name {} from bucket: {}", item.objectName(), bucketName);
                } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException | ParseException e) {
                    log.error("unable to get file from bucket {}", bucketName, e);
                    throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND,
                            "unable to get file from bucket " + bucketName, new Object[] { "error", e.getMessage() });
                }
            }
            Collections.sort(fileDetailEntities, CMP_BY_COUNTRY_ASC_DATE_DESC);
            log.info("Retrieved {} objects from bucket {}", fileDetailEntities.size(), bucketName);
        }
        return fileDetailEntities;
    }

    @Override
    public Optional<FileEntity> findContentBy(String bucketName, String name) throws FileException {
        Optional<FileEntity> optionalFileEntity =  Optional.empty();
        try {
            InputStream is = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .build());
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(name);
            fileEntity.setFilePointer(is);
            optionalFileEntity = Optional.of(fileEntity);
            log.info("Retrieved object of length {} with name {} from bucket: {}", is.available(), name, bucketName);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Error getting all available buckets: {}", e);
            throw new FileException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE,
                    "Unable to get available buckets", new Object[] {e.getMessage()});
        }
        return optionalFileEntity;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        fileDtf = DateTimeFormatter.ofPattern(fileTimestampFormat);
    }
}