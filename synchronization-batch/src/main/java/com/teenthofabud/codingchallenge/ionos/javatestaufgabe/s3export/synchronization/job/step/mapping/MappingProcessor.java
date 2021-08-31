package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.data.KundeModelVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.integration.kunde.proxy.KundeServiceClient;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.KundeAuftragDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MappingProcessor implements ItemProcessor<List<AuftragKundeDto>, List<KundeAuftragDto>> {

    private KundeServiceClient client;

    @Autowired
    public void setClient(KundeServiceClient client) {
        this.client = client;
    }

    private KundeAuftragDto convert(AuftragKundeDto item) throws Exception {
        KundeAuftragDto dto = new KundeAuftragDto();
        KundeModelVo vo = client.getKundeModelDetailsByKundenId(item.getKundeId().toString());
        dto.setFirma(vo.getFirmenName());
        dto.setLand(vo.getLand());
        dto.setNachName(vo.getNachName());
        dto.setVorName(vo.getVorName());
        dto.setPlz(vo.getPlz());
        dto.setOrt(vo.getOrt());
        dto.setStrasse(vo.getStrasse());
        dto.setStrassenZuSatz(vo.getStrassenZuSatz());
        dto.setKundeId(String.valueOf(vo.getKundenId()));
        dto.setArtikelNummer(item.getArtikelNummer());
        dto.setAuftragId(item.getAuftragId());
        log.debug("Converted {} to {} via {}", item, dto, vo);
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
