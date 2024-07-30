package org.pablos.backendcountingservice.service.exception_handling;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.ObjectViolationDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.shortic.exception.*;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlingService {
    private static final String SHORT_LINK = "shortLink";
    private static final String FULL_LINK = "fullLink";

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
    public ViolationDTO onLinkNotFoundException(LinkNotFoundException e) {
        return new ViolationDTO(SHORT_LINK, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(FullLinkNotProvidedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationDTO onFullLinkNotProvidedException(FullLinkNotProvidedException e) {
        return new ViolationDTO(FULL_LINK, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(FullLinkSizeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationDTO onFullLinkSizeException(FullLinkSizeException e) {
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
