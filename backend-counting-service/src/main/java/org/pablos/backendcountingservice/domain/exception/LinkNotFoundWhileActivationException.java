package org.pablos.backendcountingservice.domain.exception;

public class LinkNotFoundWhileActivationException extends RuntimeException{
    public LinkNotFoundWhileActivationException() {
        super("Link was not found during LinkUnit activation.");
    }
}
