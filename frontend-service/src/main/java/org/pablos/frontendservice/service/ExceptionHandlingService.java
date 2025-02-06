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
 * Класс обработки выбрасываемых исключений.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlingService {

    private final Logger logger;

    /**
     * Метод обрабатывает исключения, возникающие в процессе валидации ссылки.
     * Логгирует текст ошибки и редиректит на страницу 404.
     *
     * @param response объект ответа HTTP.
     * @param e исключение, которое нужно обработать.
     * @throws IOException если возникает ошибка ввода-вывода.
     */
    @ExceptionHandler({LinkProcessingException.class, LinkNotFoundException.class, ObjectNotProvidedException.class})
    public void onLinkProcessingException(HttpServletResponse response, Exception e) throws IOException {
        logger.warn(e.getMessage(), e);
        response.sendRedirect("/error/404");
    }

    /**
     * Метод обрабатывает исключение {@link WrongPasswordException}.
     * Логгирует текст ошибки и редиректит на страницу ошибки пароля.
     *
     * @param response объект ответа HTTP.
     * @param e исключение, которое нужно обработать.
     * @throws IOException если возникает ошибка ввода-вывода.
     */
    @ExceptionHandler({WrongPasswordException.class})
    public void onWrongPasswordException(HttpServletResponse response, Exception e) throws IOException {
        logger.warn(e.getMessage(), e);
        response.sendRedirect("/error/password");
    }

    /**
     * Метод обрабатывает исключение небезопасной ссылки {@link LinkNotSecureException}.
     * Логгирует текст ошибки и редиректит на страницу ошибки 410.
     *
     * @param response объект ответа HTTP.
     * @param e исключение, которое нужно обработать.
     * @throws IOException если возникает ошибка ввода-вывода.
     */
    @ExceptionHandler(LinkNotSecureException.class)
    public void onLinkNotSecureException(HttpServletResponse response, Exception e) throws IOException {
        logger.warn(e.getMessage(), e);
        response.sendRedirect("/error/410");
    }

    /**
     * Метод обрабатывает исключения не правильного ввода данных {@link WrongInputException}, {@link FullLinkNotProvidedException}, {@link FullLinkSizeException},
     * {@link FullLinkFormatException}.
     * Логгирует текст ошибки и редиректит на страницу ошибки 400, где выводится описание ошибки для пользователя.
     *
     * @param e исключение, которое нужно обработать.
     * @return объект ModelAndView с информацией об ошибке.
     */
    @ExceptionHandler({WrongInputException.class, FullLinkNotProvidedException.class, FullLinkSizeException.class,
            FullLinkFormatException.class})
    public ModelAndView onWrongInputException(Exception e) {
        logger.warn(e.getMessage(), e);
        ModelAndView modelAndView = new ModelAndView("/error/400");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

}
