package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.KundeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.jpa.KundeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MappingProcessor implements ItemProcessor<List<AuftragKundeDto>, List<KundeAuftragDto>> {

    private KundeRepository entityRepository;

    @Autowired
    public void setEntityRepository(KundeRepository entityRepository) {
        this.entityRepository = entityRepository;
    }


    private KundeAuftragDto convert(AuftragKundeDto item) throws Exception {
        KundeAuftragDto dto = new KundeAuftragDto();
        KundeEntity ke = entityRepository.findByKundenId(item.getKundeId());
        dto.setFirma(ke.getFirmenName());
        dto.setLand(ke.getLand());
        dto.setNachName(ke.getNachName());
        dto.setVorName(ke.getVorName());
        dto.setPlz(ke.getPlz());
        dto.setOrt(ke.getOrt());
        dto.setStrasse(ke.getStrasse());
        dto.setStrassenZuSatz(ke.getStrassenZuSatz());
        dto.setKundeId(String.valueOf(ke.getKundenId()));
        dto.setArtikelNummer(item.getArtikelNummer());
        dto.setAuftragId(item.getAuftragId());
        log.debug("Converted {} to {}", item, dto);
        return dto;
    }

    /**
     * Convert list of auftrag kunde map to list of kunde auftrag csv line item format by retrieving full details of the associated kunde id from the given auftrag kunde map
     * @param items
     * @return
     * @throws Exception
     */
    @Override
    public List<KundeAuftragDto> process(List<AuftragKundeDto> items) throws Exception {
        List<KundeAuftragDto> kundeAuftragDtoList = new ArrayList<>(items.size());
        for(AuftragKundeDto dtoSrc : items) {
            KundeAuftragDto dtoTrgt = convert(dtoSrc);
            kundeAuftragDtoList.add(dtoTrgt);
        }
        log.info("Converted {} AuftragKundeDto to {} KundeAuftragDto", items.size(), kundeAuftragDtoList.size());
        return kundeAuftragDtoList;
    }
}
