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
     * Метод обрабатывает исключения {@link LinkProcessingException}, возникающие в процессе валидации ссылки.
     * Передаёт статус ответа 404 и редиректит на страницу 404.
     * @param e выбрасываемое исключение
     * @return ответ с заголовком редиректа на 404
     */
    @ExceptionHandler(LinkProcessingException.class)
    public ResponseEntity<Void> onLinkProcessingException(LinkProcessingException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.LOCATION, "/404")
                .build();
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
     * Передаёт статус ответа 404 и редиректит на страницу 404.
     * @param e выбрасываемое исключение
     * @return ответ с заголовком редиректа на 404
     */
    @ExceptionHandler(ObjectNotProvidedException.class)
    public ResponseEntity<Void> onFastLinkDTONotProvidedException(ObjectNotProvidedException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.LOCATION, "/404")
                .build();
    }

    /**
     * Метод обрабатывает исключения {@link FullLinkNotProvidedException} и {@link FullLinkSizeException},
     * возникающие в процессе валидации полной ссылки.
     * Передаёт статус ответа 400 и редиректит на главную страницу.
     * @param e выбрасываемое исключение
     * @return ответ с заголовком редиректа на главную страницу
     */
    @ExceptionHandler({FullLinkNotProvidedException.class, FullLinkSizeException.class})
    public ResponseEntity<Void> onFullLinkExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.LOCATION, "/")
                .build();
    }

}
