package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.converter;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class AuditEntity2VoConverter implements Converter<AuditEntity, AuditVo>, InitializingBean {

    private String jobTimestampFormat;
    private DateTimeFormatter sdf;

    @Value("${s3export.download.job.file.timestamp.format:yyyy-MM-dd'_'HH-mm-ss}")
    public void setJobTimestampFormat(String jobTimestampFormat) {
        this.jobTimestampFormat = jobTimestampFormat;
    }

    @Override
    public AuditVo convert(AuditEntity entity) {
        AuditVo vo = new AuditVo();
        vo.setAction(entity.getAction());
        vo.setModule(entity.getModule());
        String createdAt = sdf.format(entity.getCreatedOn());
        vo.setCreatedAt(createdAt);
        String modifiedAt = sdf.format(entity.getModifiedOn());
        vo.setCreatedAt(modifiedAt);
        vo.setCreatedBy(String.valueOf(entity.getCreatedBy()));
        vo.setModifiedBy(String.valueOf(entity.getModifiedBy()));
        vo.setDescription(entity.getDescription());
        vo.setId(entity.getId());
        vo.setActive(entity.getActive());
        vo.setInput(entity.getInput());
        vo.setOutput(entity.getOutput());
        log.debug("Converted {} to {} ", entity, vo);
        return vo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sdf = DateTimeFormatter.ofPattern(jobTimestampFormat);
    }
}
