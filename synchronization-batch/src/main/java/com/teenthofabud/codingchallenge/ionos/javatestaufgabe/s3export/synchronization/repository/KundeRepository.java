package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.entity.KundeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface KundeRepository extends JpaRepository<KundeEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    public KundeEntity findByKundenId(Long kundenId);

}
