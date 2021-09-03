package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.service.impl;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.converter.BucketEntity2VoConverter;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.repository.BucketRepository;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.service.BucketService;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.error.DownloadErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BucketServiceImpl implements BucketService, InitializingBean {

    private BucketRepository repository;
    private BucketEntity2VoConverter entity2VoConverter;

    private DateTimeFormatter dtf;

    private String bucketTimestampFormat;

    @Value("${s3export.download.job.bucket.timestamp.format:yyyy-MM-dd}")
    public void setBucketTimestampFormat(String bucketTimestampFormat) {
        this.bucketTimestampFormat = bucketTimestampFormat;
    }

    @Autowired
    public void setEntity2VoConverter(BucketEntity2VoConverter entity2VoConverter) {
        this.entity2VoConverter = entity2VoConverter;
    }

    @Autowired
    public void setRepository(BucketRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<BucketVo> retrieveAllByNaturalOrdering() throws BucketException {
        List<BucketVo> bucketVos = new LinkedList<>();
        List<BucketEntity> bucketEntities = repository.findAll();
        if(!bucketEntities.isEmpty()) {
            bucketVos = bucketEntities.stream().map(b -> entity2VoConverter.convert(b)).collect(Collectors.toList());
        }
        log.info("Total buckets available: {}", bucketVos.size());
        return new LinkedHashSet<>(bucketVos);
    }

    @Override
    public Set<String> retrieveAllCountries() throws BucketException {
        Set<String> countryNames = new HashSet<>();
        List<BucketEntity> bucketEntities = repository.findAll();
        if(!bucketEntities.isEmpty()) {
            countryNames = bucketEntities.stream().map(b -> b.getCountry()).collect(Collectors.toSet());
        }
        log.info("Unique countries available: {}", countryNames.size());
        return new TreeSet<>(countryNames);
    }

    @Override
    public BucketVo retrieveByName(String name) throws BucketException {
        Optional<BucketEntity> bucketEntity = Optional.empty();
        List<BucketEntity> bucketEntities = repository.findAll();
        if(!bucketEntities.isEmpty()) {
            bucketEntity = bucketEntities.stream().filter(b -> b.getName().equalsIgnoreCase(name)).findAny();
        }
        if(bucketEntity.isEmpty()) {
            log.error("No bucket available with name: {}", name);
            throw new BucketException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "name", name });
        }
        BucketVo bucketVo = entity2VoConverter.convert(bucketEntity.get());
        log.info("Bucket available with name {}", bucketVo.getName());
        return bucketVo;
    }

    @Override
    public List<BucketVo> retrieveAllForCountry(String country) throws BucketException {
        List<BucketVo> bucketVos = new LinkedList<>();
        List<BucketEntity> filteredBuckets = searchByCountry(country);
        if(filteredBuckets.isEmpty()) {
            log.error("No buckets available for country {}", country);
            throw new BucketException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "country", country });
        }
        bucketVos = filteredBuckets.stream().map(b -> entity2VoConverter.convert(b)).collect(Collectors.toList());
        log.info("Retrieved {} buckets matching country {}", bucketVos.size(), country);
        return bucketVos;

    }

    @Override
    public List<BucketVo> retrieveAllForDate(String date) throws BucketException {
        List<BucketVo> bucketVos = new LinkedList<>();
        LocalDate localDate = LocalDate.now();
        try {
            localDate = LocalDate.parse(date, dtf);
        } catch (DateTimeParseException e) {
            throw new BucketException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "date", date});
        }
        final LocalDate timestamp = localDate;
        List<BucketEntity> bucketList = repository.findAll();
        List<BucketEntity> filteredBuckets = bucketList.stream()
                .filter(b -> b.getDate().isEqual(timestamp)).collect(Collectors.toList());
        if(filteredBuckets.isEmpty()) {
            log.error("No buckets available on date {}", timestamp);
            throw new BucketException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "date", timestamp });
        }
        bucketVos = filteredBuckets.stream().map(b -> entity2VoConverter.convert(b)).collect(Collectors.toList());
        log.info("Retrieved {} buckets matching with date {}", bucketVos.size(), timestamp);
        return bucketVos;
    }

    @Override
    public BucketVo retrieveLatestForCountry(String country) throws BucketException {
        List<BucketEntity> filteredBuckets = searchByCountry(country);
        if(filteredBuckets.isEmpty()) {
            log.error("No buckets available for country {}", country);
            throw new BucketException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "country", country });
        }
        BucketEntity bucketEntity = filteredBuckets.get(0);
        BucketVo bucketVo = entity2VoConverter.convert(bucketEntity);
        log.info("Retrieved {} as latest bucket for country {}", bucketVo, country);
        return bucketVo;
    }

    @Override
    public BucketVo retrieveForCountryOnDate(String country, String date) throws BucketException {
        List<BucketEntity> filteredBuckets = searchByCountry(country);
        try {
            LocalDate localDate = LocalDate.parse(date, dtf);
            Optional<BucketEntity> optionalBucketEntity = filteredBuckets.stream().filter(b -> b.getDate().isEqual(localDate)).findFirst();
            if(optionalBucketEntity.isEmpty()) {
                log.error("No buckets available for country {} on date {}", country, date);
                throw new BucketException(DownloadErrorCode.DOWNLOAD_NOT_FOUND, new Object[] { "country: " + country, "date: " + date });
            }
            BucketVo bucketVo = entity2VoConverter.convert(optionalBucketEntity.get());
            log.info("Retrieved {} as latest bucket for country {} on date {}", bucketVo, country, date);
            return bucketVo;
        } catch (DateTimeParseException e) {
            log.error("Invalid date {}", date);
            throw new BucketException(DownloadErrorCode.DOWNLOAD_ATTRIBUTE_INVALID, new Object[] { "date", date });
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dtf = DateTimeFormatter.ofPattern(bucketTimestampFormat);
    }

    private List<BucketEntity> searchByCountry(String country) throws BucketException {
        List<BucketEntity> bucketList = repository.findAll();
        List<BucketEntity> filteredBuckets = bucketList.stream()
                .filter(b -> b.getCountry().equalsIgnoreCase(country)).collect(Collectors.toList());
        return filteredBuckets;
    }

}
