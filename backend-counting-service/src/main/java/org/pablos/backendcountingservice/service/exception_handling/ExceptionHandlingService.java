package org.pablos.backendcountingservice.service.exception_handling;

import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.ObjectViolationDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.backendgivingservice.domain.exception.LinkNotFoundException;
import org.pablos.backendgivingservice.domain.exception.LinkProcessingException;
import org.pablos.backendgivingservice.service.exception_handling.dto.ValidationErrorResponse;
import org.pablos.backendgivingservice.service.exception_handling.dto.Violation;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.exception.ObjectNotProvidedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ExceptionHandlingService {
    private static final String SHORT_LINK = "shortLink";

    /**
     * Метод обрабатывает исключения {@link LinkProcessingException}, возникающие в процессе валидации ссылки.
     * Передаёт статус ответа 400 и объект {@link ViolationDTO}, содержащий имя поля с ошибкой и сообщение.
     * @param e выбрасываемое исключение
     * @return {@link ViolationDTO}
     */
    @ResponseBody
    @ExceptionHandler(LinkProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationDTO onLinkProcessingException(LinkProcessingException e) {
        return new ViolationDTO(SHORT_LINK, e.getMessage());
    }

    /**
     * Метод обрабатывает исключения {@link ObjectNotProvidedException}, возникающие когда объект не передан.
     * Передаёт статус ответа 400 и объект {@link ObjectViolationDTO}, содержащий имя объекта и сообщение.
     * @param e выбрасываемое исключение
     * @return {@link ObjectViolationDTO}
     */
    @ResponseBody
    @ExceptionHandler(ObjectNotProvidedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ObjectViolationDTO onFastLinkDTONotProvidedException(ObjectNotProvidedException e) {
        return new ObjectViolationDTO(FastLinkDTO.class.getSimpleName(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onLinkNotFoundException(LinkNotFoundException e) {
        final List<ViolationDTO> violations =
                List.of(new ViolationDTO(FastLink.class.getFields()[0].getName(), e.getMessage()));
        return new ValidationErrorResponse(violations);
    }
}
