package org.pablos.shortic.exception;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException() {
        super("Password must be exactly 5 digits");

    }
}