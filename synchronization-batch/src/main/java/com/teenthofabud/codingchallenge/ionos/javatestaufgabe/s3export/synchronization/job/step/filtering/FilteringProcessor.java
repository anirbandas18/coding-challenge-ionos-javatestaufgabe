package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.filtering;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FilteringProcessor implements ItemProcessor<List<AuftraegeEntity>, List<AuftragKundeDto>> {

    /**
     * Convert list of auftraege entity to list of auftrag kunde map
     * @param items
     * @return
     * @throws Exception
     */

    @Override
    public List<AuftragKundeDto> process(List<AuftraegeEntity> items) throws Exception {
        List<AuftragKundeDto> auftragKundeDtos = new ArrayList<>(items.size());
        for(AuftraegeEntity item : items) {
            AuftragKundeDto akDto = new AuftragKundeDto();
            akDto.setAuftragId(item.getId().getAuftragId());
            akDto.setArtikelNummer(item.getId().getArtikelNummber());
            akDto.setKundeId(Long.parseLong(item.getId().getKundenId()));
            log.debug("Converted {} to {}", item, akDto);
            auftragKundeDtos.add(akDto);
        }
        log.info("Processed {} AuftraegeEntity into {} AuftragKundeDto" , items.size(), auftragKundeDtos.size());
        return auftragKundeDtos;
    }
}
