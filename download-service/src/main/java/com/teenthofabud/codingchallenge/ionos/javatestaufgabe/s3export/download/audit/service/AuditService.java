package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface AuditService {

    public Set<AuditVo> retrieveAllByNaturalOrdering();

    public AuditVo retrieveDetailsById(Long id) throws AuditException;

    public List<AuditVo> retrieveAllMatchingDetailsByAction(String action) throws AuditException;

    public List<AuditVo> retrieveAllMatchingDetailsByModule(String module) throws AuditException;

    public List<AuditVo> retrieveAllMatchingDetailsByModuleAndAction(String module, String action) throws AuditException;

    public Long createAudit(AuditForm form) throws AuditException;

}
