package org.pablos.shortic.exception;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException() {
        super("Wrong password");
    }
}
