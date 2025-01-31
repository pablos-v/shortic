package org.pablos.common.exception;

public class ObjectNotProvidedException extends RuntimeException{
    public ObjectNotProvidedException() {
        super("Object was not provided");
    }
}

