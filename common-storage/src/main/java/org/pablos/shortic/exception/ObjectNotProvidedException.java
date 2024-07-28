package org.pablos.shortic.exception;

public class ObjectNotProvidedException extends RuntimeException{
    public ObjectNotProvidedException() {
        super("Object was not provided");
    }
}

