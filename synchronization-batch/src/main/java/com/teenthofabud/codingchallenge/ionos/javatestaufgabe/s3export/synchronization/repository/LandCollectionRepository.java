package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.data.dto.LandCollectionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandCollectionRepository extends CrudRepository<LandCollectionDto, String> {

    public LandCollectionDto findByCollectionKey(String collectionKey);

}
