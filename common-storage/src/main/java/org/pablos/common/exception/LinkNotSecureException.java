package org.pablos.common.exception;

public class LinkNotSecureException extends RuntimeException{
    public LinkNotSecureException() {
        super("Provided link is unsecure");
    }
}
