package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.pablos.common.exception.LinkNotFoundException;
import org.pablos.common.exception.LinkProcessingException;
import org.pablos.common.exception.WrongInputException;

/**
 * Интерфейс для работы с модулем отдачи полной ссылки по короткой backend-giving-service.
 */
public interface IGivingService {

    /**
     * Обрабатывает клик по ссылке: отправляет данные клика для записи статистики клика
     * и возвращает полную ссылку.
     *
     * @param shortLink сокращённая ссылка
     * @param request объект запроса
     * @return полную ссылку
     * @throws LinkNotFoundException если ссылка не найдена
     * @throws WrongInputException если входные данные некорректны
     * @throws LinkProcessingException если произошла ошибка при обработке ссылки
     */
    String clickProcessing(String shortLink, HttpServletRequest request) throws LinkNotFoundException,
            WrongInputException, LinkProcessingException;
}
