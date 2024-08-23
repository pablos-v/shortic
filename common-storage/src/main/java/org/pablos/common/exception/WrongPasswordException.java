package org.pablos.common.exception;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException() {
        super("Wrong password");
    }
}
