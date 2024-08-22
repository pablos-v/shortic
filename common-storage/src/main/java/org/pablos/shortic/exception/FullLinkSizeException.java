package org.pablos.shortic.exception;

public class FullLinkSizeException extends RuntimeException {
    public FullLinkSizeException() {
        super("Full link size exceeds the limit");
    }
}