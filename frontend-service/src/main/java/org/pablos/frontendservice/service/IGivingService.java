package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.exception.WrongInputException;

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
