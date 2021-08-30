package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.repository;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileDetailEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileEntity;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.file.data.FileException;
import org.reflections.vfs.Vfs;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository {

    /**
     * Get all file references for the given bucket sorted by name ascending and date descending
     * @param bucketName
     * @return
     * @throws FileException
     */
    public List<FileDetailEntity> findAllReferencesBy(String bucketName) throws FileException;

    /**
     * Get raw contents of the file by the specified name on the given bucket
     * @param bucketName
     * @param name
     * @return
     * @throws FileException
     */
    public Optional<FileEntity> findContentBy(String bucketName, String name) throws FileException;

}
