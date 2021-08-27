package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.mapping;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.AuftragKundeDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.KundeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.KundeAuftragVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.KundeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@StepScope
public class MappingProcessor implements ItemProcessor<AuftragKundeDto, KundeAuftragVo> {

    private KundeRepository entityRepository;

    @Autowired
    public void setEntityRepository(KundeRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    /**
     * Convert auftrag kunde map to auftrag kunde csv line item format by retrieving full details of the associated kunde id from the given kunde auftrag map
     * @param item
     * @return
     * @throws Exception
     */
    @Override
    public KundeAuftragVo process(AuftragKundeDto item) throws Exception {
        KundeAuftragVo vo = new KundeAuftragVo();
        KundeEntity ke = entityRepository.findByKundenId(item.getKundeId());
        vo.setFirma(ke.getFirmenName());
        vo.setLand(ke.getLand());
        vo.setNachName(ke.getNachName());
        vo.setVorName(ke.getVorName());
        vo.setPlz(ke.getPlz());
        vo.setOrt(ke.getOrt());
        vo.setStrasse(ke.getStrasse());
        vo.setStrassenZuSatz(ke.getStrassenZuSatz());
        vo.setKundeId(String.valueOf(ke.getKundenId()));
        vo.setArtikelNummer(item.getArtikelNummer());
        vo.setAuftragId(item.getAuftragId());
        return vo;
    }
}
