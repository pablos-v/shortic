package org.pablos.common.exception;

public class FullLinkFormatException extends RuntimeException{
    public FullLinkFormatException() {
        super("Link must start with http:// or https:// and end with .someDomain");
    }

}
