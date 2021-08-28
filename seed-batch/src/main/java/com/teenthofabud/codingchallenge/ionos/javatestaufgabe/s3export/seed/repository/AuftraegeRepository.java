package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.data.entity.AuftraegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface AuftraegeRepository extends JpaRepository<AuftraegeEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public AuftraegeEntity save(AuftraegeEntity entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Boolean existsByIdAuftragId(String auftragId);

}
