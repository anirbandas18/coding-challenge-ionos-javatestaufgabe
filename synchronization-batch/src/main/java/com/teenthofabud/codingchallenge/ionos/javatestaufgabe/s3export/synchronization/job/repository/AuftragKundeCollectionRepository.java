package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.AuftragKundeCollectionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuftragKundeCollectionRepository extends CrudRepository<AuftragKundeCollectionDto, String> {

    public AuftragKundeCollectionDto findByCollectionKey(String collectionKey);

}
