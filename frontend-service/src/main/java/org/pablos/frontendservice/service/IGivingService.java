package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.pablos.common.exception.LinkNotFoundException;
import org.pablos.common.exception.LinkProcessingException;
import org.pablos.common.exception.WrongInputException;

public interface IGivingService {
    /**
     * Обрабатывает клик по ссылке: отправляет данные клика для записи статистики клика
     * и возвращает полную ссылку.
     * @param shortLink Сокращённая ссылка
     * @param request Объект запроса
     * @return Полную ссылку
     */
    String clickProcessing(String shortLink, HttpServletRequest request) throws LinkNotFoundException,
            WrongInputException, LinkProcessingException;
}
