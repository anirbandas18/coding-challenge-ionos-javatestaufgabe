package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data.AuftraegeModelPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuftraegeModelRepository extends JpaRepository<AuftraegeModelEntity, AuftraegeModelPrimaryKey> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public AuftraegeModelEntity save(AuftraegeModelEntity entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Boolean existsByPrimaryKeyAuftragIdAndPrimaryKeyArtikelNummerAndPrimaryKeyKundenId(String auftragId, String artikelNummer, String kundenId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<AuftraegeModelEntity> findByPrimaryKeyAuftragIdAndPrimaryKeyArtikelNummerAndPrimaryKeyKundenId(String auftragId, String artikelNummer, String kundenId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    public Optional<AuftraegeModelEntity> findByPrimaryKeyAuftragId(String auftragId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<AuftraegeModelEntity> findByPrimaryKeyArtikelNummerContaining(String artikelNummer);

    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<AuftraegeModelEntity> findByPrimaryKeyKundenIdContaining(String kundenId);

    @Transactional
    @Query(name = "AuftraegeModelEntity.findAllWithinTheLastNHourNowAsPerUTC", nativeQuery = true)
    public List<AuftraegeModelEntity> findAllWithinTheLastNHourNowAsPerUTC(@Param("amount") Long amount);

    @Transactional
    @Query(name = "AuftraegeModelEntity.findAllWithinTheLastNMinuteNowAsPerUTC", nativeQuery = true)
    public List<AuftraegeModelEntity> findAllWithinTheLastNMinuteNowAsPerUTC(@Param("amount") Long amount);

    @Transactional
    @Query(name = "AuftraegeModelEntity.findAllWithinTheLastNSecondNowAsPerUTC", nativeQuery = true)
    public List<AuftraegeModelEntity> findAllWithinTheLastNSecondNowAsPerUTC(@Param("amount") Long amount);


}
