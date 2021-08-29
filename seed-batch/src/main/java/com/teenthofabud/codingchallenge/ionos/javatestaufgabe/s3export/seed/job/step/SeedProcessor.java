package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.data.entity.AuftraegeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.data.entity.AuftraegeEntityPrimaryKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class SeedProcessor implements ItemProcessor<AuftraegeEntity, AuftraegeEntity>, InitializingBean {

    public Long kundeIdLowerBound;
    public Long kundeIdUpperBound;
    public Integer createdYearUpperBound;
    public Integer createdYearLowerBound;
    public Integer lastChangeYearUpperBound;
    public Integer lastChangeYearLowerBound;
    private String timestampFormat;
    private String timezone;
    private DateTimeFormatter dtf;

    @Value("${s3export.seed.timezone:Europe/Paris}")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Value("${s3export.seed.job.timestamp.format}")
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    @Value("${s3export.seed.kundeid.lower.bound:1}")
    public void setKundeIdLowerBound(Long kundeIdLowerBound) {
        this.kundeIdLowerBound = kundeIdLowerBound;
    }

    @Value("${s3export.seed.kundeid.upper.bound:100}")
    public void setKundeIdUpperBound(Long kundeIdUpperBound) {
        this.kundeIdUpperBound = kundeIdUpperBound;
    }

    @Value("${s3export.seed.created.year.upper.bound:2019}")
    public void setCreatedYearUpperBound(Integer createdYearUpperBound) {
        this.createdYearUpperBound = createdYearUpperBound;
    }

    @Value("${s3export.seed.created.year.lower.bound:2017}")
    public void setCreatedYearLowerBound(Integer createdYearLowerBound) {
        this.createdYearLowerBound = createdYearLowerBound;
    }

    @Value("${s3export.seed.lastchange.year.upper.bound:2021}")
    public void setLastChangeYearUpperBound(Integer lastChangeYearUpperBound) {
        this.lastChangeYearUpperBound = lastChangeYearUpperBound;
    }

    @Value("${s3export.seed.lastchange.year.lower.bound:2020}")
    public void setLastChangeYearLowerBound(Integer lastChangeYearLowerBound) {
        this.lastChangeYearLowerBound = lastChangeYearLowerBound;
    }

    private String getAuftragId() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE));
    }

    private String getKundeId() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(kundeIdLowerBound, kundeIdUpperBound + 1));
    }

    private Integer getCreatedYear() {
        return ThreadLocalRandom.current().nextInt(createdYearLowerBound, createdYearUpperBound + 1);
    }

    private Integer getLastChangeYear() {
        return ThreadLocalRandom.current().nextInt(lastChangeYearLowerBound, lastChangeYearUpperBound + 1);
    }

    private Integer getMonth() {
        Integer month = 2;
        while(month == 2) {
            month = ThreadLocalRandom.current().nextInt(1, 12 + 1);
        }
        return month;
    }

    private Integer getDayOfMonth(Integer dayLowerBound) {
        return ThreadLocalRandom.current().nextInt(1, dayLowerBound + 1);
    }

    private String getArtikelNummer() {
        return String.valueOf(System.nanoTime());
    }

    private Integer getHour() {
        return ThreadLocalRandom.current().nextInt(0, 23 + 1);
    }

    private Integer getMinute() {
        return ThreadLocalRandom.current().nextInt(0, 59 + 1);
    }

    private Integer getSecond() {
        return ThreadLocalRandom.current().nextInt(0, 59 + 1);
    }

    private String getTimetamp(Integer year) {
        Integer month = getMonth();
        Integer dayLowerBound = -1;
        if(Year.isLeap(year) && month == 2) {
            dayLowerBound = 28;
        } else {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    dayLowerBound = 31;
                    break;
                case 2:
                    dayLowerBound = 29;
                    break;
                default:
                    dayLowerBound = 30;
                    break;
            }
        }
        Integer dayOfMonth = getDayOfMonth(dayLowerBound);
        Integer hour = getHour();
        Integer minute = getMinute();
        Integer second = getSecond();
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoneDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(timezone));
        //LocalDateTime utcDateTime = utcZoneDateTime.toLocalDateTime();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(timestampFormat);
        String timestamp = utcZoneDateTime.format(dtf);
        return timestamp;
    }

    private String getLastChange() {
        Integer year = getLastChangeYear();
        String lastChangeTimestamp = getTimetamp(year);
        return lastChangeTimestamp;
    }

    private String getCreated() {
        Integer year = getCreatedYear();
        String createdTimestamp = getTimetamp(year);
        return createdTimestamp;
    }

    private String getCurrentTimestamp() {
        OffsetDateTime utcNow = OffsetDateTime.now(ZoneOffset.UTC);
        ZonedDateTime utcZoneDateTime = utcNow.toZonedDateTime();
        String timestamp = utcZoneDateTime.format(dtf);
        return timestamp;
    }

    @Override
    public AuftraegeEntity process(AuftraegeEntity item) throws Exception {
        AuftraegeEntityPrimaryKey pk = item.getId();
        pk.setArtikelNummber(getArtikelNummer());
        pk.setAuftragId(getAuftragId());
        pk.setKundenId(getKundeId());
        item.setCreated(getCurrentTimestamp());
        item.setLastChange(getCurrentTimestamp());
        item.setId(pk);
        return item;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dtf = DateTimeFormatter.ofPattern(timestampFormat);
    }
}
