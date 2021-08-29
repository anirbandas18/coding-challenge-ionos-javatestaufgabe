package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.error;

public class SeedException /*extends TOABBaseException*/ extends Exception{

    public SeedException(String message) {
        super(message);
    }

    /*public SeedException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public SeedException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
    }

    public SeedException(TOABError error, String message) {
        super(error, message);
    }

    public SeedException(TOABError error, Object[] parameters) {
        super(error, parameters);
    }

    @Override
    public String getSubDomain() {
        return "job";
    }*/
}
