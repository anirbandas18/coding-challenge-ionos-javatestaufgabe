package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.step;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.job.data.AuftraegeModelPrimaryKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class AuftraegeSeedProcessor implements ItemProcessor<AuftraegeModelEntity, AuftraegeModelEntity>, InitializingBean {

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

    @Value("${s3export.seed.kundenid.lower.bound:101}")
    public void setKundeIdLowerBound(Long kundeIdLowerBound) {
        this.kundeIdLowerBound = kundeIdLowerBound;
    }

    @Value("${s3export.seed.kundenid.upper.bound:200}")
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

    private String getArtikelNummer() {
        return String.valueOf(System.nanoTime());
    }

    private String getCurrentTimestamp() {
        OffsetDateTime utcNow = OffsetDateTime.now(ZoneOffset.UTC);
        ZonedDateTime utcZoneDateTime = utcNow.toZonedDateTime();
        String timestamp = utcZoneDateTime.format(dtf);
        return timestamp;
    }

    @Override
    public AuftraegeModelEntity process(AuftraegeModelEntity item) throws Exception {
        AuftraegeModelPrimaryKey pk = item.getPrimaryKey();
        pk.setArtikelNummber(getArtikelNummer());
        pk.setAuftragId(getAuftragId());
        pk.setKundenId(getKundeId());
        item.setCreated(getCurrentTimestamp());
        item.setLastChange(getCurrentTimestamp());
        item.setPrimaryKey(pk);
        return item;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dtf = DateTimeFormatter.ofPattern(timestampFormat);
    }
}
