package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.error;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuftraegeSeedException extends TOABBaseException {

    @ToString.Include
    private transient TOABError error;

    public AuftraegeSeedException(String message) {
        super(message);
    }

    public AuftraegeSeedException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public AuftraegeSeedException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
        this.error = error;
    }

    public AuftraegeSeedException(TOABError error, Object[] parameters) {
        super(error, parameters);
        this.error = error;
    }

    @Override
    public String getSubDomain() {
        return "Auftraege";
    }

}
