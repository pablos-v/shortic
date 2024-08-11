package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.pablos.frontendservice.exception.WrongInputException;
import org.pablos.shortic.exception.*;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

/**
 * Класс обработки выбрасываемых исключений
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlingService {

    private final Logger logger;

    /**
     * TODO Метод обрабатывает исключения {@link LinkProcessingException}, возникающие в процессе валидации ссылки.
     * Передаёт статус ответа 404 и редиректит на страницу 404.
     * @return ответ с заголовком редиректа на 404
     */
    @ExceptionHandler({LinkProcessingException.class, LinkNotFoundException.class, ObjectNotProvidedException.class})
    public void onLinkProcessingException(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Not Found: {}", e.getMessage(), e);
        response.sendRedirect("/error/404");
    }

    @ExceptionHandler({WrongPasswordException.class, PasswordIncorrectException.class})
    public void onWrongPasswordException(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Password Incorrect: {}", e.getMessage(), e);
        response.sendRedirect("/error/password");
    }

    @ExceptionHandler(LinkNotSecureException.class)
    public void onLinkNotSecureException(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Link Not Secure: {}", e.getMessage(), e);
        response.sendRedirect("/error/410");
    }

    @ExceptionHandler(WrongInputException.class)
    public void onWrongInputException(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Input data incorrect: {}", e.getMessage(), e);
        response.sendRedirect("/error/400");
    }


    /**
     * Метод обрабатывает исключения {@link FullLinkNotProvidedException} и {@link FullLinkSizeException},
     * возникающие в процессе валидации полной ссылки.
     * Передаёт статус ответа 400 и редиректит на главную страницу.
     * @return ответ с заголовком редиректа на главную страницу
     */
    @ExceptionHandler({FullLinkNotProvidedException.class, FullLinkSizeException.class, FullLinkFormatException.class})
    public void onFullLinkExceptions(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Full link not correct: {}", e.getMessage(), e);
        response.sendRedirect("/");
    }

}
