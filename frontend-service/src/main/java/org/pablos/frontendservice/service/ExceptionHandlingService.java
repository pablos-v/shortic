package org.pablos.frontendservice.service;

import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.ObjectViolationDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.shortic.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Класс обработки выбрасываемых исключений
 */
@ControllerAdvice
public class ExceptionHandlingService {


    /**
     * TODO Метод обрабатывает исключения {@link LinkProcessingException}, возникающие в процессе валидации ссылки.
     * Передаёт статус ответа 404 и редиректит на страницу 404.
     * @return ответ с заголовком редиректа на 404
     */
    @ExceptionHandler({LinkProcessingException.class, LinkNotFoundException.class, ObjectNotProvidedException.class})
    public ResponseEntity<Void> onLinkProcessingException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.LOCATION, "/404")
                .build();
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<Void> onWrongPasswordException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.LOCATION, "/400")
                .build();
    }

    /**
     * Метод обрабатывает исключения {@link FullLinkNotProvidedException} и {@link FullLinkSizeException},
     * возникающие в процессе валидации полной ссылки.
     * Передаёт статус ответа 400 и редиректит на главную страницу.
     * @return ответ с заголовком редиректа на главную страницу
     */
    @ExceptionHandler({FullLinkNotProvidedException.class, FullLinkSizeException.class})
    public ResponseEntity<Void> onFullLinkExceptions() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.LOCATION, "/")
                .build();
    }

}
