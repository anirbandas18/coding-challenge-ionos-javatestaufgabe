package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.dto.FileBucketCollectionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileNameLocationCollectionRepository extends CrudRepository<FileBucketCollectionDto, String> {

    public FileBucketCollectionDto findByCollectionKey(String collectionKey);

}
