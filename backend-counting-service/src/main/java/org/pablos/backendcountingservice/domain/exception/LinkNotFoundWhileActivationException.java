package org.pablos.backendcountingservice.domain.exception;

/**
 * Исключение, которое выбрасывается при попытке активации ссылки, которая не была найдена.
 */
public class LinkNotFoundWhileActivationException extends RuntimeException{

    public LinkNotFoundWhileActivationException() {
        super("Link was not found during LinkUnit activation.");
    }
}

