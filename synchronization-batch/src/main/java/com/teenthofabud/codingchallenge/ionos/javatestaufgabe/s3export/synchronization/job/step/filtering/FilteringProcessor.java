package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@StepScope
public class FilteringProcessor implements ItemProcessor<AuftraegeEntity, AuftragKundeDto> {

    /**
     * Convert auftraege entity to auftrag-kunde map
     * @param item
     * @return
     * @throws Exception
     */

    @Override
    public AuftragKundeDto process(AuftraegeEntity item) throws Exception {
        AuftragKundeDto akDto = new AuftragKundeDto();
        akDto.setAuftragId(item.getId().getAuftragId());
        akDto.setArtikelNummer(item.getId().getArtikelNummber());
        akDto.setKundeId(Long.parseLong(item.getId().getKundenId()));
        log.info("Converted {} to {}", item, akDto);
        return akDto;
    }
}
