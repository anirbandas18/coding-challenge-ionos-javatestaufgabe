package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuftraegeModelEntity2VoConverter implements Converter<AuftraegeModelEntity, AuftraegeModelVo> {
    @Override
    public AuftraegeModelVo convert(AuftraegeModelEntity entity) {
        AuftraegeModelVo vo = new AuftraegeModelVo();
        vo.setKundenId(entity.getPrimaryKey().getKundenId());
        vo.setAuftragId(entity.getPrimaryKey().getAuftragId());
        vo.setArtikelNummer(entity.getPrimaryKey().getArtikelNummer());
        vo.setCreated(entity.getCreated());
        vo.setLastChange(entity.getLastChange());
        log.debug("Converted {} to {} ", entity, vo);
        return vo;
    }
}
