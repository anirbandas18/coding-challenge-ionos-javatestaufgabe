package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.error;

public class SynchronizationException /*extends TOABBaseException*/ extends Exception {

    public SynchronizationException(String message) {
        super(message);
    }

    /*public SynchronizationException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public SynchronizationException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
    }

    public SynchronizationException(TOABError error, String message) {
        super(error, message);
    }

    public SynchronizationException(TOABError error, Object[] parameters) {
        super(error, parameters);
    }

    @Override
    public String getSubDomain() {
        return "job";
    }*/
}
