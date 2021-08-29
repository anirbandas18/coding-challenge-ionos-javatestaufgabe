package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.step.segmentation;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.LandKundenDto;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.KundeAuftragVo;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.vo.LandKundeAuftragCollectionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.util.*;

@Slf4j
public class SegmentationProcessor implements ItemProcessor<List<LandKundenDto>, List<LandKundeAuftragCollectionVo>> {

    private StepExecution stepExecution;
    private String jobParameterName1;

    @Value("${s3export.sync.job.parameter.1:synchronization-timestamp}")
    public void setJobParameterName1(String jobParameterName1) {
        this.jobParameterName1 = jobParameterName1;
    }


    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    private Long getJobExecutionTimestamp() throws ParseException {
        JobParameters jobParameters = stepExecution.getJobParameters();
        Map<String, JobParameter> parametersMap = jobParameters.getParameters();
        JobParameter parameter = parametersMap.get(jobParameterName1);
        String jobParameterValue1 = parameter.getValue().toString();
        Long jobTimeStamp = Long.parseLong(jobParameterValue1);
        return jobTimeStamp;
    }

    private KundeAuftragVo convert(KundeAuftragDto dto) {
        KundeAuftragVo vo = new KundeAuftragVo();
        vo.setFirma(dto.getFirma());
        vo.setArtikelNummer(dto.getArtikelNummer());
        vo.setAuftragId(dto.getAuftragId());
        vo.setKundeId(dto.getKundeId());
        vo.setNachName(dto.getNachName());
        vo.setOrt(dto.getOrt());
        vo.setStrasse(dto.getStrasse());
        vo.setPlz(dto.getPlz());
        vo.setVorName(dto.getVorName());
        vo.setStrassenZuSatz(dto.getStrassenZuSatz());
        vo.setLand(dto.getLand());
        log.debug("Converting {} to {}", dto, vo);
        return vo;
    }

    /**
     * Mapping land kunde auftrag map dto directly with its associated land along with kunde auftrag collection along with job execution timestamp
     * @param items
     * @return
     * @throws Exception
     */
    @Override
    public List<LandKundeAuftragCollectionVo> process(List<LandKundenDto> items) throws Exception {
        List<LandKundeAuftragCollectionVo> voList = new ArrayList<>(items.size());
        for(LandKundenDto item : items) {
            List<KundeAuftragVo> csvLineItems = new ArrayList<>(item.getAssociatedKundenAuftrag().size());
            List<KundeAuftragDto> associatedKundeAuftrag = item.getAssociatedKundenAuftrag();
            for(KundeAuftragDto dto : associatedKundeAuftrag) {
                KundeAuftragVo vo = convert(dto);
                csvLineItems.add(vo);
            }
            Long jobExecutionTimestamp = getJobExecutionTimestamp();
            LandKundeAuftragCollectionVo vo = new LandKundeAuftragCollectionVo();
            vo.setLand(item.getLand());
            vo.setTimestamp(jobExecutionTimestamp);
            vo.setItems(csvLineItems);
            log.debug("Associated {} LandKundeDto items with land: {} key to LandKundeAuftragCollection for csv line item using timestamp: {}",
                    csvLineItems.size(), item.getLand(), jobExecutionTimestamp);
            voList.add(vo);
        }

        log.info("Processed {} LandKundenDto into {} LandKundeAuftragCollectionVo" , items.size(), voList.size());
        return voList;
    }
}
