package org.pablos.backendcountingservice.service.exception_handling;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.shortic.exception.LinkNotSecureException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.shortic.exception.WrongPasswordException;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.ObjectViolationDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.shortic.exception.*;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlingService {
    private static final String SHORT_LINK = "shortLink";
    private static final String FULL_LINK = "fullLink";
    public static final String PASSWORD = "password";

    private final Logger logger;


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
        logger.error("Link not correct: {}", e.getMessage(), e);
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
        logger.error("No Object was Provided: {}", e.getMessage(), e);
        return new ObjectViolationDTO(FastLinkDTO.class.getSimpleName(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ViolationDTO onLinkNotFoundException(LinkNotFoundException e) {
        logger.error("Link Not Found: {}", e.getMessage(), e);
        return new ViolationDTO(SHORT_LINK, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(FullLinkNotProvidedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationDTO onFullLinkNotProvidedException(FullLinkNotProvidedException e) {
        logger.error("Full link is absent: {}", e.getMessage(), e);
        return new ViolationDTO(FULL_LINK, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(PasswordIncorrectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationDTO onPasswordIncorrectException(PasswordIncorrectException e) {
        logger.error("Password Incorrect: {}", e.getMessage(), e);
        return new ViolationDTO(PASSWORD, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(FullLinkSizeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationDTO onFullLinkSizeException(FullLinkSizeException e) {
        logger.error("Full Link Size Incorrect: {}", e.getMessage(), e);
        return new ViolationDTO(FULL_LINK, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ViolationDTO onWrongPasswordException(WrongPasswordException e) {
        logger.error("Wrong Password: {}", e.getMessage(), e);
        return new ViolationDTO(PASSWORD, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(LinkNotSecureException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ViolationDTO onLinkNotSecureException(LinkNotSecureException e) {
        logger.error("Link Not Secure: {}", e.getMessage(), e);
        return new ViolationDTO(FULL_LINK, e.getMessage());
    }

    @ExceptionHandler(LinkNotFoundWhileActivationException.class)
    public void onLinkNotFoundWhileActivationException(LinkNotFoundWhileActivationException e) {
        logger.error("Link was not found during LinkUnit activation: {}", e.getMessage(), e);
    }
    @ExceptionHandler(SavingFastLinkException.class)
    public void onSavingFastLinkException(SavingFastLinkException e) {
        logger.error("Link saving failed: {}", e.getMessage(), e);
    }
    @ExceptionHandler(DeletingFastLinkException.class)
    public void onDeletingFastLinkException(DeletingFastLinkException e) {
        logger.error("Link deleting failed: {}", e.getMessage(), e);
    }


}
