package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download;

import com.adobe.testing.s3mock.S3MockApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.KundeAuftragVo;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class DownloadIntegrationBaseTest {

    private static final String FILE_EXTENSION_DELIMITTER = ".";

    private MinioClient minioClient;
    protected ObjectMapper om;

    private Integer bucketPort;
    private String bucketNamePrefix;
    private String bucketNameDelimitter;
    private String timezone;
    private String bucketTimestampFormat;
    protected Set<String> landCollection;
    private Long daysToAdd;
    private Long itemsPerLand;
    private Long itemsPerBucket;
    private String fileTimestampFormat;
    private String fileNameDelimitter;
    private String fileExtension;
    private String bucketBaseUrl;
    private String bucketAccessKey;
    private String bucketSecretKey;

    @Value("${s3export.download.bucket.base.url}")
    public void setBucketBaseUrl(String bucketBaseUrl) {
        this.bucketBaseUrl = bucketBaseUrl;
    }

    @Value("${s3export.download.bucket.access.key}")
    public void setBucketAccessKey(String bucketAccessKey) {
        this.bucketAccessKey = bucketAccessKey;
    }

    @Value("${s3export.download.bucket.secret.key}")
    public void setBucketSecretKey(String bucketSecretKey) {
        this.bucketSecretKey = bucketSecretKey;
    }

    @Value("${s3export.download.items.per.bucket}")
    public void setItemsPerBucket(Long itemsPerBucket) {
        this.itemsPerBucket = itemsPerBucket;
    }

    @Value("${s3export.download.job.file.timestamp.format}")
    public void setFileTimestampFormat(String fileTimestampFormat) {
        this.fileTimestampFormat = fileTimestampFormat;
    }

    @Value("${s3export.download.delimitter.file.name}")
    public void setFileNameDelimitter(String fileNameDelimitter) {
        this.fileNameDelimitter = fileNameDelimitter;
    }

    @Value("${s3export.download.export.file.extension}")
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Value("${s3export.download.items.per.land}")
    public void setItemsPerLand(Long itemsPerLand) {
        this.itemsPerLand = itemsPerLand;
    }

    @Value("${s3export.download.days.toadd}")
    public void setDaysToAdd(Long daysToAdd) {
        this.daysToAdd = daysToAdd;
    }

    @Value("#{'${s3export.download.land.list}'.split(',')}")
    public void setLandCollection(Set<String> landCollection) {
        this.landCollection = landCollection;
    }

    @Value("${s3export.download.job.bucket.timestamp.format}")
    public void setBucketTimestampFormat(String bucketTimestampFormat) {
        this.bucketTimestampFormat = bucketTimestampFormat;
    }

    @Value("${s3export.download.timezone}")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Value("${s3export.download.delimitter.bucket.name}")
    public void setBucketNameDelimitter(String bucketNameDelimitter) {
        this.bucketNameDelimitter = bucketNameDelimitter;
    }

    @Value("${s3export.download.job.bucket.name.prefix}")
    public void setBucketNamePrefix(String bucketNamePrefix) {
        this.bucketNamePrefix = bucketNamePrefix;
    }


    @Value("${s3export.download.bucket.port}")
    public void setBucketPort(Integer bucketPort) {
        this.bucketPort = bucketPort;
    }

    @Autowired
    public void setOm(ObjectMapper om) {
        this.om = om;
    }

    private Faker faker;
    private S3MockApplication s3MockServer;
    private DateTimeFormatter bucketSdf;
    private DateTimeFormatter fileSdf;
    private Set<String> bucketNames;

    private String getBucketName(String country, Long timestamp) {
        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.of(timezone)).toLocalDate();
        String date = bucketSdf.format(localDate);
        String bucketName = String.join(bucketNameDelimitter, bucketNamePrefix, country, date);
        return bucketName;
    }

    private KundeAuftragVo getKundeAuftragMapping(String country, String userId) {
        KundeAuftragVo vo = new KundeAuftragVo();
        Address address = faker.address();
        Name name = faker.name();
        vo.setAuftragId(String.valueOf(System.nanoTime()));
        vo.setKundeId(userId);
        vo.setArtikelNummer(UUID.randomUUID().toString());
        vo.setFirma(faker.company().name());
        vo.setLand(country);
        vo.setNachName(name.lastName());
        vo.setVorName(name.firstName());
        vo.setOrt(address.cityName());
        vo.setPlz(address.zipCode());
        vo.setStrasse(address.streetName());
        vo.setStrassenZuSatz(address.secondaryAddress());
        return vo;
    }

    private String getFileName(String country, Long timestamp) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.of(timezone)).toLocalDateTime();
        String synchronizationDateStr = fileSdf.format(localDateTime);
        StringBuffer sbf = new StringBuffer();
        sbf.append(country);
        sbf.append(fileNameDelimitter);
        sbf.append(synchronizationDateStr);
        sbf.append(FILE_EXTENSION_DELIMITTER);
        sbf.append(fileExtension);
        return sbf.toString();
    }

    protected Map<String, List<String>> initBuckets(Boolean objectsAsWell) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        if(minioClient == null) {
            minioClient = MinioClient.builder()
                    .endpoint(bucketBaseUrl).credentials(bucketAccessKey, bucketSecretKey)
                    .build();
        }

        bucketNames = new TreeSet<>();

        Map<String, List<String>> countryFileMap = new TreeMap<>();

        for(String land : landCollection) {
            Long timestamp = System.currentTimeMillis();
            String bucketName = getBucketName(land, timestamp);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            bucketNames.add(bucketName);
            if(objectsAsWell) {
                for(int i = 0 ; i < itemsPerBucket ; i++) {
                    List<KundeAuftragVo> voList = new LinkedList<>();
                    for(int j = 0 ; j < itemsPerLand ; j++) {
                        KundeAuftragVo vo = getKundeAuftragMapping(land, String.valueOf(j + 1));
                        voList.add(vo);
                    }
                    timestamp = LocalDateTime.now().plusHours(daysToAdd).atZone(ZoneId.of(timezone)).toInstant().getEpochSecond();
                    String csvFileName = getFileName(land, timestamp);
                    Writer writer  = new StringWriter();
                    StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .build();
                    sbc.write(voList);
                    String csvFileContent = writer.toString();
                    writer.close();
                    InputStream is = new ByteArrayInputStream(csvFileContent.getBytes());
                    minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(csvFileName).stream(is, is.available(), -1).build());
                    List<String> objectNames = countryFileMap.containsKey(land) ? countryFileMap.get(land) : new LinkedList<>();
                    objectNames.add(csvFileName);
                    countryFileMap.put(land, objectNames);
                }
            }
        }
        return countryFileMap;
    }

    protected void destroyBuckets(Boolean objectsAsWell) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        if(minioClient == null) {
            minioClient = MinioClient.builder()
                    .endpoint(bucketBaseUrl).credentials(bucketAccessKey, bucketSecretKey)
                    .build();
        }

        for(String bucketName : bucketNames) {
            if(objectsAsWell) {
                Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
                for(Result<Item> object : objects) {
                    String objectName = object.get().objectName();
                    minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
                }
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        }
    }

    @BeforeAll
    private void setUp() throws InterruptedException {
        s3MockServer = S3MockApplication.start("--server.port=0", "--http.port=" + bucketPort);
        bucketSdf = DateTimeFormatter.ofPattern(bucketTimestampFormat);
        fileSdf = DateTimeFormatter.ofPattern(fileTimestampFormat);
        faker = new Faker();
        om.registerModule(new Jdk8Module());
        om.registerModule(new JavaTimeModule());
        landCollection = new TreeSet<>(landCollection);
    }

    @AfterAll
    private void tearDown() {
        if(s3MockServer != null) {
            s3MockServer.stop();
        }
    }

}
