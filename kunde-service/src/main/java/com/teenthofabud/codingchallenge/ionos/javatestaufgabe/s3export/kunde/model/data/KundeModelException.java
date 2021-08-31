package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KundeModelException extends TOABBaseException {

    @ToString.Include
    private transient TOABError error;

    public KundeModelException(String message) {
        super(message);
    }

    public KundeModelException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public KundeModelException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
        this.error = error;
    }

    public KundeModelException(TOABError error, Object[] parameters) {
        super(error, parameters);
        this.error = error;
    }

    @Override
    public String getSubDomain() {
        return "Model";
    }

}
