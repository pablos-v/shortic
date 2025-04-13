package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.UpdatingFastLinkException;
import org.pablos.common.dto.ViolationDTO;
import org.pablos.common.exception.*;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс для обработки исключений, возникающих в процессе работы приложения.
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
    @ExceptionHandler({LinkProcessingException.class, ObjectNotProvidedException.class, WrongInputException.class,
            FullLinkNotProvidedException.class, FullLinkSizeException.class, FullLinkFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onLinkProcessingException(Exception e) {
        logger.warn(e.getMessage(), e);
        return e.getMessage();
    }

    /**
     * Метод обрабатывает исключения {@link LinkNotFoundException}, возникающие при попытке активации ссылки, которая не была найдена.
     * Передаёт статус ответа 404 и сообщение об ошибке.
     * @param e выбрасываемое исключение
     * @return сообщение об ошибке
     */
    @ResponseBody
    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onLinkNotFoundException(LinkNotFoundException e) {
        logger.warn(e.getMessage(), e);
        return e.getMessage();
    }

    /**
     * Метод обрабатывает исключения {@link WrongPasswordException}, возникающие при попытке активации ссылки с неправильным паролем.
     * Передаёт статус ответа 401 и сообщение об ошибке.
     * @param e выбрасываемое исключение
     * @return сообщение об ошибке
     */
    @ResponseBody
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String onWrongPasswordException(WrongPasswordException e) {
        logger.warn(e.getMessage(), e);
        return e.getMessage();
    }

    /**
     * Метод обрабатывает исключения {@link LinkNotFoundWhileActivationException}, возникающие при попытке активации ссылки, которая не была найдена.
     * Логирует ошибку.
     * @param e выбрасываемое исключение
     */
    @ExceptionHandler(LinkNotFoundWhileActivationException.class)
    public void onLinkNotFoundWhileActivationException(LinkNotFoundWhileActivationException e) {
        logger.error(e.getMessage(), e);
    }

    /**
     * Метод обрабатывает исключения {@link SavingFastLinkException}, {@link UpdatingFastLinkException}, {@link DeletingFastLinkException},
     * возникающие при попытке сохранения, обновления или удаления быстрой ссылки.
     * Логирует ошибку.
     * @param e выбрасываемое исключение
     */
    @ExceptionHandler({SavingFastLinkException.class, UpdatingFastLinkException.class, DeletingFastLinkException.class})
    public void onAnyChangingFastLinkException(Exception e) {
        logger.error(e.getMessage(), e);
    }

}
