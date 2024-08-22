package org.pablos.shortic.exception;

public class LinkNotSecureException extends RuntimeException{
    public LinkNotSecureException() {
        super("Provided link is unsecure");
    }
}
