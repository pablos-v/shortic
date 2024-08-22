package org.pablos.shortic.exception;


public class LinkNotFoundException extends RuntimeException{
    public LinkNotFoundException(String message) {
        super(message);
    }
    public LinkNotFoundException() {
        super("Link was not found");
    }
}

