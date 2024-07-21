package org.pablos.backendgivingservice.domain.exception.handling;

import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.backendgivingservice.domain.exception.LinkNotFoundException;
import org.pablos.backendgivingservice.domain.exception.handling.dto.ValidationErrorResponse;
import org.pablos.backendgivingservice.domain.exception.handling.dto.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ResponseBody
    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onConstraintValidationException(LinkNotFoundException e) {
        final List<Violation> violations = List.of(new Violation(FastLink.class.getFields()[0].getName(), e.getMessage()));
        return new ValidationErrorResponse(violations);
    }
}
