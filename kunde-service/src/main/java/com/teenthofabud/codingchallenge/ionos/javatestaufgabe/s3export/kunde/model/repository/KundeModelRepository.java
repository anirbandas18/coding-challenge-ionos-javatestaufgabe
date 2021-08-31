package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface KundeModelRepository extends JpaRepository<KundeModelEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    public Optional<KundeModelEntity> findByKundenId(Long kundenId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<KundeModelEntity> findByEmailContaining(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Boolean existsByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public KundeModelEntity save(KundeModelEntity entity);


}
