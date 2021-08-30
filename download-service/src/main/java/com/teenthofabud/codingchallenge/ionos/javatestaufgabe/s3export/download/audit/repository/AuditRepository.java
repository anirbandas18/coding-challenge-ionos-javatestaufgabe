package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data.AuditEntity;
import com.teenthofabud.core.common.repository.TOABAdvancedEntityBaseRepository;
import com.teenthofabud.core.common.repository.TOABSimpleEntityBaseRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;


@Repository
public interface AuditRepository extends TOABSimpleEntityBaseRepository<AuditEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public AuditEntity save(AuditEntity entity);

    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<AuditEntity> findByModuleContaining(String module);

    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<AuditEntity> findByActionContaining(String action);

    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<AuditEntity> findByModuleAndAction(String module, String action);


}
