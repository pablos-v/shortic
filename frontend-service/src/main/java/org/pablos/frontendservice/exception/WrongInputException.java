package org.pablos.frontendservice.exception;

public class WrongInputException extends RuntimeException{
    public WrongInputException(String message) {
        super(message);
    }
    public WrongInputException() {
    }

}
