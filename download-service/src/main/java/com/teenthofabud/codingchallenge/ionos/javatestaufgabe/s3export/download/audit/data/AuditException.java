package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.audit.data;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuditException extends TOABBaseException {

    @ToString.Include
    private transient TOABError error;

    public AuditException(String message) {
        super(message);
    }

    public AuditException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public AuditException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
        this.error = error;
    }

    public AuditException(TOABError error, Object[] parameters) {
        super(error, parameters);
        this.error = error;
    }

    @Override
    public String getSubDomain() {
        return "Audit";
    }

}
