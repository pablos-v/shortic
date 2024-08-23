package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.pablos.common.exception.*;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

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
        logger.warn(e.getMessage(), e);
        response.sendRedirect("/error/404");
    }

    @ExceptionHandler({WrongPasswordException.class})
    public void onWrongPasswordException(HttpServletResponse response, Exception e) throws IOException {
        logger.warn(e.getMessage(), e);
        response.sendRedirect("/error/password");
    }

    @ExceptionHandler(LinkNotSecureException.class)
    public void onLinkNotSecureException(HttpServletResponse response, Exception e) throws IOException {
        logger.warn(e.getMessage(), e);
        response.sendRedirect("/error/410");
    }

    @ExceptionHandler({WrongInputException.class, FullLinkNotProvidedException.class, FullLinkSizeException.class,
            FullLinkFormatException.class})
    public ModelAndView onWrongInputException(Exception e) {
        logger.warn(e.getMessage(), e);
        ModelAndView modelAndView = new ModelAndView("/error/400");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    /**
     * Метод обрабатывает исключения {@link FullLinkNotProvidedException} и {@link FullLinkSizeException},
     * возникающие в процессе валидации полной ссылки.
     * Передаёт статус ответа 400 и редиректит на главную страницу.
     * @return ответ с заголовком редиректа на главную страницу
     */
//    @ExceptionHandler({FullLinkNotProvidedException.class, FullLinkSizeException.class, FullLinkFormatException.class})
//    public void onFullLinkExceptions(HttpServletResponse response, Exception e) throws IOException {
//        logger.error(e.getMessage(), e);
//        response.sendRedirect("/");
//    }

}
