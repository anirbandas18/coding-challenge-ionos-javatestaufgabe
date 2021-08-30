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

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class FileServiceImpl implements FileService {

    private FileRepository repository;
    private FileEntity2VoConverter entity2VoConverter;
    private BucketService service;

    //private DateTimeFormatter dtf;
    //private SimpleDateFormat bucketSdf;

    private String bucketNamePrefix;
    private String bucketNameDelimitter;
    private String bucketTimestampFormat;

    @Value("${s3export.download.job.bucket.timestamp.format:yyyy-MM-dd}")
    public void setBucketTimestampFormat(String bucketTimestampFormat) {
        this.bucketTimestampFormat = bucketTimestampFormat;
    }

    @Value("${s3export.download.job.bucket.name.prefix:s3export-synchronization-batch}")
    public void setBucketNamePrefix(String bucketNamePrefix) {
        this.bucketNamePrefix = bucketNamePrefix;
    }

    @Value("${s3export.download.delimitter.bucket.name:_}")
    public void setBucketNameDelimitter(String bucketNameDelimitter) {
        this.bucketNameDelimitter = bucketNameDelimitter;
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

    /*@Override
    public Set<FileVo> retrieveAllByNaturalOrdering() throws FileException {
        List<FileVo> fileVos = new LinkedList<>();
        List<FileEntity> bucketEntities = repository.findAll();
        if(!bucketEntities.isEmpty()) {
            fileVos = bucketEntities.stream().map(b -> entity2VoConverter.convert(b)).collect(Collectors.toList());
        }
        log.info("Total buckets available: {}", fileVos.size());
        return new LinkedHashSet<>(fileVos);
    }

    @Override
    public FileVo retrieveByName(String name) throws FileException {
        Optional<FileEntity> bucketEntity = Optional.empty();
        List<FileEntity> bucketEntities = repository.findAll();
        if(!bucketEntities.isEmpty()) {
            bucketEntity = bucketEntities.stream().filter(b -> b.getName().equalsIgnoreCase(name)).findAny();
        }
        if(bucketEntity.isEmpty()) {
            log.error("No bucket available with name: {}", name);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "name", name });
        }
        FileVo fileVo = entity2VoConverter.convert(bucketEntity.get());
        log.info("Bucket available with name {}", fileVo.getName());
        return fileVo;
    }

    @Override
    public List<FileVo> retrieveAllForCountry(String country) throws FileException {
        List<FileVo> fileVos = new LinkedList<>();
        List<FileEntity> filteredBuckets = searchByCountry(country);
        if(filteredBuckets.isEmpty()) {
            log.error("No buckets available for country {}", country);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "country", country });
        }
        fileVos = filteredBuckets.stream().map(b -> entity2VoConverter.convert(b)).collect(Collectors.toList());
        log.info("Retrieved {} buckets matching country {}", fileVos.size(), country);
        return fileVos;

    }

    @Override
    public List<FileVo> retrieveAllForDate(String date) throws FileException {
        List<FileVo> fileVos = new LinkedList<>();
        LocalDate localDate = LocalDate.now();
        try {
            localDate = LocalDate.parse(date, dtf);
        } catch (DateTimeParseException e) {
            throw new FileException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "date", date});
        }
        final LocalDate timestamp = localDate;
        List<FileEntity> bucketList = repository.findAll();
        List<FileEntity> filteredBuckets = bucketList.stream()
                .filter(b -> b.getDate().isEqual(timestamp)).collect(Collectors.toList());
        if(filteredBuckets.isEmpty()) {
            log.error("No buckets available on date {}", timestamp);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "date", timestamp });
        }
        fileVos = filteredBuckets.stream().map(b -> entity2VoConverter.convert(b)).collect(Collectors.toList());
        log.info("Retrieved {} buckets matching with date {}", fileVos.size(), timestamp);
        return fileVos;
    }*/


    @Override
    public FileVo retrieveBy(String country, String date, String name) throws FileException {
        throw new FileException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE, "not implemented",
                new Object[] { "functionality not available" });
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
    public FileVo retrieveBy(String country, String timestamp) throws FileException {
        throw new FileException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE, "not implemented",
                new Object[] { "functionality not available" });
    }

    /*@Override
    public FileVo retrieveLatestForCountry(String country) throws FileException {
        List<FileEntity> filteredBuckets = searchByCountry(country);
        if(filteredBuckets.isEmpty()) {
            log.error("No buckets available for country {}", country);
            throw new FileException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "country", country });
        }
        FileEntity fileEntity = filteredBuckets.get(filteredBuckets.size() - 1);
        FileVo fileVo = entity2VoConverter.convert(fileEntity);
        log.info("Retrieved {} as latest bucket for country {}", fileVo, country);
        return fileVo;
    }*/

    /*@Override
    public FileVo retrieveForCountryOnDate(String country, String date) throws FileException {
        throw new FileException(DownloadErrorCode.DOWNLOAD_ACTION_FAILURE, "not implemented",
                new Object[] { "functionality not available" });
    }*/

    /*@Override
    public void afterPropertiesSet() throws Exception {
        dtf = DateTimeFormatter.ofPattern(bucketTimestampFormat);
        bucketSdf = new SimpleDateFormat(bucketTimestampFormat);
    }*/

    /*private List<FileEntity> searchByCountry(String country) throws FileException {
        String bucketNamePattern = String.join(bucketNameDelimitter, bucketNamePrefix, country);
        List<FileEntity> bucketList = repository.fi
        List<FileEntity> filteredBuckets = bucketList.stream()
                .filter(b -> b.getCountry().startsWith(bucketNamePattern)).collect(Collectors.toList());
        return filteredBuckets;
    }*/

    /*private String getBucketName(String country, Long timestamp) {
        Date dt = new Date();
        dt.setTime(timestamp);
        String date = bucketSdf.format(dt);
        String bucketName = String.join(bucketNameDelimitter, bucketNamePrefix, country, date);
        return bucketName;
    }*/

}
