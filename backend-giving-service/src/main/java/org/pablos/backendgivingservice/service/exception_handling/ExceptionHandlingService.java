package org.pablos.backendgivingservice.service.exception_handling;

import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.shortic.exception.ObjectNotProvidedException;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.dto.ObjectViolationDTO;
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
     * Метод обрабатывает исключения {@link LinkNotFoundException}, возникающие когда ссылка не найдена в БД.
     * Передаёт статус ответа 404 и объект {@link ViolationDTO}, содержащий имя поля с ошибкой и сообщение.
     * @param e выбрасываемое исключение
     * @return {@link ViolationDTO}
     */
    @ResponseBody
    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ViolationDTO onLinkNotFoundException(LinkNotFoundException e) {
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
}
