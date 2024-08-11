package org.pablos.shortic.exception;

public class FullLinkFormatException extends RuntimeException{
    public FullLinkFormatException(String message) {
        super(message);
    }
    public FullLinkFormatException() {
        super("Link must start with http:// or https:// and end with .someDomain");
    }

}
