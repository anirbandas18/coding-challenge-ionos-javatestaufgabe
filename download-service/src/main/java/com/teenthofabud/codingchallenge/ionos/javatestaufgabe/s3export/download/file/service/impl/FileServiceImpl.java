package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.service.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.service.BucketService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.converter.FileEntity2VoConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileDetailEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.repository.FileRepository;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class FileServiceImpl implements FileService, InitializingBean {

    private FileRepository repository;
    private FileEntity2VoConverter entity2VoConverter;
    private BucketService service;

    private String bucketTimestampFormat;

    private DateTimeFormatter dtf;

    @Value("${s3export.download.job.bucket.timestamp.format:yyyy-MM-dd}")
    public void setBucketTimestampFormat(String bucketTimestampFormat) {
        this.bucketTimestampFormat = bucketTimestampFormat;
    }

    @Autowired
    public void setEntity2VoConverter(FileEntity2VoConverter entity2VoConverter) {
        this.entity2VoConverter = entity2VoConverter;
    }

    @Autowired
    public void setRepository(FileRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setService(BucketService service) {
        this.service = service;
    }

    @Override
    public FileVo retrieveBy(String country, String date, String name) throws FileException {
        BucketVo bucketForCountryOnDate = null;
        try {
            bucketForCountryOnDate = service.retrieveForCountryOnDate(country, date);
        } catch (BucketException e) {
            log.error("No file available for country on the given date", e);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND,
                    "No file available for country on the given date", new Object [] { "country: " + country, "date: " + date });
        }
        String bucketName = bucketForCountryOnDate.getName();
        log.info("Bucket available for country {} on date {} as {}", country, date, bucketName);

        Optional<FileEntity> fileEntityOptional = repository.findContentBy(bucketName, name);
        if(fileEntityOptional.isEmpty()) {
            log.error("No file content available for country {} on date {}", country, date);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND,
                    "no file content available for country on the given date", new Object [] { "country: " + country, "date: " + date });
        }
        FileVo fileVo = entity2VoConverter.convert(fileEntityOptional.get());
        log.info("Retrieved file reference of size {} for the file of country {} on date {}", fileVo.getContent().length, country, date);
        return fileVo;
    }

    @Override
    public FileVo retrieveBy(String country) throws FileException {
        BucketVo latestBucketForCountry = null;
        try {
            latestBucketForCountry = service.retrieveLatestForCountry(country);
        } catch (BucketException e) {
            log.error("No files available for country", e);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND,
                    "no files available for country", new Object [] { "country", country });
        }
        log.info("Latest bucket for country {} is {}", country, latestBucketForCountry.getName());
        List<FileDetailEntity> fileDetailEntities = repository.findAllReferencesBy(latestBucketForCountry.getName());
        FileDetailEntity latestFileDetail = fileDetailEntities.get(0);
        log.info("Latest file in bucket for country {} is {}", latestFileDetail);
        Optional<FileEntity> optionalFileEntity = repository.findContentBy(latestBucketForCountry.getName(), latestFileDetail.getName());
        if(optionalFileEntity.isEmpty()) {
            log.error("No file available for country {}", country);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND,
                    "no file available for country", new Object [] { "country", country });
        }
        FileEntity fileEntity = optionalFileEntity.get();
        log.info("Raw content for file {} is available at bucket {}", fileEntity, latestFileDetail.getName());
        FileVo fileVo = entity2VoConverter.convert(fileEntity);
        log.info("Download the raw content for file {}", fileVo);
        return fileVo;
    }


    @Override
    public FileVo retrieveBy(String country, String date) throws FileException {
        BucketVo bucketForCountryOnDate = null;
        try {
            bucketForCountryOnDate = service.retrieveForCountryOnDate(country, date);
        } catch (BucketException e) {
            log.error("No file available for country on the given date", e);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND,
                    "No file available for country on the given date", new Object [] { "country: " + country, "date: " + date });
        }
        String bucketName = bucketForCountryOnDate.getName();
        log.info("Bucket available for country {} on date {} as {}", country, date, bucketName);
        List<FileDetailEntity> fileDetailEntities = repository.findAllReferencesBy(bucketForCountryOnDate.getName());
        LocalDateTime localDateTime = LocalDateTime.parse(date, dtf);
        Optional<FileDetailEntity> optionalFileDetailEntity = fileDetailEntities.stream().filter(f -> f.getDateTime().isEqual(localDateTime)).findFirst();
        if(optionalFileDetailEntity.isEmpty()) {
            log.error("No file reference available for country {} on date {}", country, date);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND,
                    "no file reference available for country on the given date", new Object [] { "country: " + country, "date: " + date });
        }
        FileDetailEntity fileDetailEntity = optionalFileDetailEntity.get();
        log.info("File details available for country {} on date {} as {}", country, date, fileDetailEntity.getName());
        Optional<FileEntity> fileEntityOptional = repository.findContentBy(bucketName, fileDetailEntity.getName());
        if(fileEntityOptional.isEmpty()) {
            log.error("No file content available for country {} on date {}", country, date);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND,
                    "no file content available for country on the given date", new Object [] { "country: " + country, "date: " + date });
        }
        FileVo fileVo = entity2VoConverter.convert(fileEntityOptional.get());
        log.info("Retrieved file reference of size {} for the file of country {} on date {}", fileVo.getContent().length, country, date);
        return fileVo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dtf = DateTimeFormatter.ofPattern(bucketTimestampFormat);
    }
}
