package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.jpa;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.AuftraegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AuftraegeRepository extends JpaRepository<AuftraegeEntity, Long> {

    @Transactional
    @Query(value = "select * from s3export_sync_db.auftraege where TIMESTAMPDIFF(hour, utc_timestamp, str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000')) <= :amount", nativeQuery = true)
    public List<AuftraegeEntity> findAllWithinTheLastNHourNowAsPerUTC(@Param("amount") Long amount);

    @Transactional
    @Query(value = "select * from s3export_sync_db.auftraege where TIMESTAMPDIFF(minute, utc_timestamp, str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000')) <= :amount", nativeQuery = true)
    public List<AuftraegeEntity> findAllWithinTheLastNMinuteNowAsPerUTC(@Param("amount") Long amount);

    @Transactional
    @Query(value = "select * from s3export_sync_db.auftraege where TIMESTAMPDIFF(second, utc_timestamp, str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000')) <= :amount", nativeQuery = true)
    public List<AuftraegeEntity> findAllWithinTheLastNSecondNowAsPerUTC(@Param("amount") Long amount);


}
