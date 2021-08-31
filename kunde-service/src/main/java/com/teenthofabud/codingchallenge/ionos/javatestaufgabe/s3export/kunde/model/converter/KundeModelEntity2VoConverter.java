package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KundeModelEntity2VoConverter implements Converter<KundeModelEntity, KundeModelVo> {
    @Override
    public KundeModelVo convert(KundeModelEntity entity) {
        KundeModelVo vo = new KundeModelVo();
        vo.setEmail(entity.getEmail());
        vo.setKundenId(entity.getKundenId());
        vo.setFirmenName(entity.getFirmenName());
        vo.setLand(entity.getLand());
        vo.setOrt(entity.getOrt());
        vo.setNachName(entity.getNachName());
        vo.setPlz(entity.getPlz());
        vo.setStrasse(entity.getStrasse());
        vo.setVorName(entity.getVorName());
        vo.setStrassenZuSatz(entity.getStrassenZuSatz());
        log.debug("Converted {} to {} ", entity, vo);
        return vo;
    }
}
