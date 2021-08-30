package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BucketRepository {

    public List<BucketEntity> findAll() throws BucketException;

    public Optional<BucketEntity> findByName(String name) throws BucketException;

}
