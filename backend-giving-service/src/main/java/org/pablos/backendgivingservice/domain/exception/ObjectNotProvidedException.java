package org.pablos.backendgivingservice.domain.exception;

public class ObjectNotProvidedException extends RuntimeException{
    public ObjectNotProvidedException() {
        super("Object was not provided");
    }
}

