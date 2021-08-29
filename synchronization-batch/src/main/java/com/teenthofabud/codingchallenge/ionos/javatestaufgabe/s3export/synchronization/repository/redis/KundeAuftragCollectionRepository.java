package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository.redis;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.KundeAuftragCollectionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KundeAuftragCollectionRepository extends CrudRepository<KundeAuftragCollectionDto, String> {

    public KundeAuftragCollectionDto findByCollectionKey(String collectionKey);

}
