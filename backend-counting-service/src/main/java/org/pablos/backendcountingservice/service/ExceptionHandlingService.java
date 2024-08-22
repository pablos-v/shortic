package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.UpdatingFastLinkException;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.shortic.exception.*;
import org.pablos.shortic.util.CommonUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    @ResponseBody
    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onLinkNotFoundException(LinkNotFoundException e) {
        logger.warn(e.getMessage(), e);
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String onWrongPasswordException(WrongPasswordException e) {
        logger.warn(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(LinkNotFoundWhileActivationException.class)
    public void onLinkNotFoundWhileActivationException(LinkNotFoundWhileActivationException e) {
        logger.error(e.getMessage(), e);
    }
    @ExceptionHandler({SavingFastLinkException.class, UpdatingFastLinkException.class, DeletingFastLinkException.class})
    public void onAnyChangingFastLinkException(Exception e) {
        logger.error(e.getMessage(), e);
    }

}
