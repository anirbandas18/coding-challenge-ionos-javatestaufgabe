package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.error;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KundeSeedException extends TOABBaseException {

    @ToString.Include
    private transient TOABError error;

    public KundeSeedException(String message) {
        super(message);
    }

    public KundeSeedException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public KundeSeedException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
        this.error = error;
    }

    public KundeSeedException(TOABError error, Object[] parameters) {
        super(error, parameters);
        this.error = error;
    }

    @Override
    public String getSubDomain() {
        return "Kunde";
    }

}
