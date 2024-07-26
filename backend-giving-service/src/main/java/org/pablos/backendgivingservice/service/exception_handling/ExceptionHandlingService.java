package org.pablos.backendgivingservice.service.exception_handling;

import org.pablos.FastLinkDTO;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.backendgivingservice.domain.exception.ObjectNotProvidedException;
import org.pablos.backendgivingservice.domain.exception.LinkNotFoundException;
import org.pablos.backendgivingservice.domain.exception.LinkProcessingException;
import org.pablos.backendgivingservice.service.exception_handling.dto.FieldViolationDTO;
import org.pablos.backendgivingservice.service.exception_handling.dto.ObjectViolationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс обработки выбрасываемых исключений
 */
@ControllerAdvice
public class ExceptionHandlingService {

    private static final String SHORT_LINK = "shortLink";

    /**
     * Метод обрабатывает исключения {@link LinkProcessingException}, возникающие в процессе валидации ссылки.
     * Передаёт статус ответа 400 и объект {@link FieldViolationDTO}, содержащий имя поля с ошибкой и сообщение.
     * @param e выбрасываемое исключение
     * @return {@link FieldViolationDTO}
     */
    @ResponseBody
    @ExceptionHandler(LinkProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FieldViolationDTO onLinkProcessingException(LinkProcessingException e) {
        return new FieldViolationDTO(SHORT_LINK, e.getMessage());
    }

    /**
     * Метод обрабатывает исключения {@link LinkNotFoundException}, возникающие когда ссылка не найдена в БД.
     * Передаёт статус ответа 404 и объект {@link FieldViolationDTO}, содержащий имя поля с ошибкой и сообщение.
     * @param e выбрасываемое исключение
     * @return {@link FieldViolationDTO}
     */
    @ResponseBody
    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public FieldViolationDTO onLinkNotFoundException(LinkNotFoundException e) {
        return new FieldViolationDTO(SHORT_LINK, e.getMessage());
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
}
