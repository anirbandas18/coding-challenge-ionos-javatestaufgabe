package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege.model.data;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuftraegeModelException extends TOABBaseException {

    @ToString.Include
    private transient TOABError error;

    public AuftraegeModelException(String message) {
        super(message);
    }

    public AuftraegeModelException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public AuftraegeModelException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
        this.error = error;
    }

    public AuftraegeModelException(TOABError error, Object[] parameters) {
        super(error, parameters);
        this.error = error;
    }

    @Override
    public String getSubDomain() {
        return "Model";
    }

}
