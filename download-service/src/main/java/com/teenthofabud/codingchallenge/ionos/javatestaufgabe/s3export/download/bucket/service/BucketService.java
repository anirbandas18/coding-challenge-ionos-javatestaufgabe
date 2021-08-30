package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data.BucketVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface BucketService {

    public Set<BucketVo> retrieveAllByNaturalOrdering() throws BucketException;

    public BucketVo retrieveByName(String name)  throws BucketException;

    public List<BucketVo> retrieveAllForCountry(String country)  throws BucketException;

    public List<BucketVo> retrieveAllForDate(String date)  throws BucketException;

    public BucketVo retrieveLatestForCountry(String country)  throws BucketException;

    public BucketVo retrieveForCountryOnDate(String country, String date)  throws BucketException;

}
