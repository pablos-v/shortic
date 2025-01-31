package org.pablos.backendgivingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.common.dto.ViolationDTO;
import org.pablos.common.exception.*;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс обработки выбрасываемых исключений
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlingService {

    private final Logger logger;

    /**
     * Метод обрабатывает исключения {@link LinkProcessingException}, возникающие в процессе валидации ссылки.
     * Передаёт статус ответа 400 и объект {@link ViolationDTO}, содержащий имя поля с ошибкой и сообщение.
     * @param e выбрасываемое исключение
     * @return {@link ViolationDTO}
     */
    @ResponseBody
    @ExceptionHandler({LinkProcessingException.class, FullLinkSizeException.class, FullLinkFormatException.class,
            ObjectNotProvidedException.class, FullLinkNotProvidedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onLinkProcessingException(Exception e) {
        logger.warn(e.getMessage(), e);
        return e.getMessage();
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
    public String onLinkNotFoundException(Exception e) {
        logger.warn(e.getMessage(), e);
        return e.getMessage();
    }
}
