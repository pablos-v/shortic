package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.pablos.frontendservice.exception.WrongInputException;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;

public interface IGivingService {
    /**
     * Обрабатывает клик по ссылке: отправляет данные клика для записи статистики клика
     *
     * @param shortLink
     * @param request
     * @return
     */
    String clickProcessing(String shortLink, HttpServletRequest request) throws LinkNotFoundException,
            WrongInputException, LinkProcessingException;
}
