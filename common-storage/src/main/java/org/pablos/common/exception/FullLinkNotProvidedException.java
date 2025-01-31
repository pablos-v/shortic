package org.pablos.common.exception;

public class FullLinkNotProvidedException extends RuntimeException {
    public FullLinkNotProvidedException() {
        super("Full link was not provided");
    }
}
