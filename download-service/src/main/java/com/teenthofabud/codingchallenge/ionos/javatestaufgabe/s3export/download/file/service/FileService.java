package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileVo;
import org.springframework.stereotype.Service;

@Service
public interface FileService {

    /**
     * Get the file for country on specified date with name
     * @param country
     * @param date
     * @param name
     * @return
     * @throws FileException
     */
    public FileVo retrieveBy(String country, String date, String name)  throws FileException;

    /**
     * Get the latest file by date for the country
     * @param country
     * @return
     * @throws FileException
     */
    public FileVo retrieveBy(String country)  throws FileException;

    /**
     * Get the specific file for the country on the specified datd
     * @param country
     * @param date
     * @return
     * @throws FileException
     */
    public FileVo retrieveBy(String country, String date)  throws FileException;

}