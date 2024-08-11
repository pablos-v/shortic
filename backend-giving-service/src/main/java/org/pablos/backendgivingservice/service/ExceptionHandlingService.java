package org.pablos.backendgivingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.shortic.exception.*;
import org.pablos.shortic.util.CommonUtil;
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
    @ExceptionHandler({LinkProcessingException.class, FullLinkSizeException.class, FullLinkFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationDTO onLinkProcessingException(Exception e) {
        logger.error("Link is not correct: {}", e.getMessage(), e);
        return new ViolationDTO(CommonUtil.SHORT_LINK, e.getMessage());
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
    public ViolationDTO onLinkNotFoundException(Exception e) {
        logger.error("Link Not Found: {}", e.getMessage(), e);
        return new ViolationDTO(CommonUtil.SHORT_LINK, e.getMessage());
    }

    /**
     * Метод обрабатывает исключения {@link ObjectNotProvidedException}, возникающие когда объект не передан.
     * Передаёт статус ответа 400 и объект {@link ViolationDTO}, содержащий имя объекта и сообщение.
     * @param e выбрасываемое исключение
     * @return {@link ViolationDTO}
     */
    @ResponseBody
    @ExceptionHandler({ObjectNotProvidedException.class, FullLinkNotProvidedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationDTO onFastLinkDTONotProvidedException(Exception e) {
        logger.error("No Object was Provided: {}", e.getMessage(), e);
        return new ViolationDTO(CommonUtil.OBJECT, e.getMessage());
    }
}
