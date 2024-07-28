package org.pablos.shortic.exception;

public class LinkNotFoundException extends RuntimeException{
    public LinkNotFoundException() {
        super("Link was not found");
    }
}

