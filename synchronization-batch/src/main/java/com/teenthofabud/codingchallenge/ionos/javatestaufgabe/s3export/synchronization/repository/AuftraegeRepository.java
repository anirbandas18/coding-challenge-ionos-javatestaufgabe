package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.KundeEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface AuftraegeRepository extends JpaRepository<AuftraegeEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "", nativeQuery = true)
    public List<AuftraegeEntity> findAllWithinTheLastNHourFromNowAsPerUTC(@Param("hour") Long hour);

}
