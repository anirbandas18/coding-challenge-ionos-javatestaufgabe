package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BucketException extends TOABBaseException {

    @ToString.Include
    private transient TOABError error;

    public BucketException(String message) {
        super(message);
    }

    public BucketException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public BucketException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
        this.error = error;
    }

    public BucketException(TOABError error, Object[] parameters) {
        super(error, parameters);
        this.error = error;
    }

    @Override
    public String getSubDomain() {
        return "Bucket";
    }

}
