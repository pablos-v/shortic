package org.pablos.backendgivingservice.domain.exception;

public class LinkNotFoundException extends RuntimeException{
    public LinkNotFoundException() {
        super("Link not found");
    }
}

