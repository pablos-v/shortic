package org.pablos.backendcountingservice.service.exception_handling;

import org.pablos.backendcountingservice.service.exception_handling.dto.ViolationDTO;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.backendgivingservice.domain.exception.LinkNotFoundException;
import org.pablos.backendgivingservice.domain.exception.LinkProcessingException;
import org.pablos.backendgivingservice.service.exception_handling.dto.ValidationErrorResponse;
import org.pablos.backendgivingservice.service.exception_handling.dto.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ExceptionHandlingService {

    @ResponseBody
    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onLinkNotFoundException(LinkNotFoundException e) {
        final List<ViolationDTO> violations =
                List.of(new ViolationDTO(FastLink.class.getFields()[0].getName(), e.getMessage()));
        return new ValidationErrorResponse(violations);
    }
}
