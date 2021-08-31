package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.KundeAuftragCollectionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KundeAuftragCollectionRepository extends CrudRepository<KundeAuftragCollectionDto, String> {

    public KundeAuftragCollectionDto findByCollectionKey(String collectionKey);

}
