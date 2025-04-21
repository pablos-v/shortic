package org.pablos.frontendservice.service;

import org.pablos.common.dto.ClickDTO;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.dto.LinkUnitDTO;
import org.pablos.common.dto.PageDTO;
import org.pablos.common.exception.*;

/**
 * Интерфейс для работы с модулем подсчета кликов backend-counting-service.
 */
public interface ICountingService {

    /**
     * Создаёт ссылку.
     *
     * @param input объект DTO для передачи данных в сервис
     * @return объект DTO созданной ссылки
     * @throws WrongInputException если входные данные некорректны
     * @throws FullLinkNotProvidedException если полная ссылка не была предоставлена
     * @throws FullLinkSizeException если размер полной ссылки превышает допустимый
     * @throws FullLinkFormatException если формат полной ссылки некорректен
     */
    LinkUnitDTO createLink(FastLinkDTO input) throws WrongInputException, FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException;

    /**
     * Возвращает страницу со статистикой кликов по ссылке.
     *
     * @param page номер страницы
     * @param size размер страницы
     * @param shortLink короткая ссылка
     * @param password пароль
     * @return объект DTO со статистикой кликов
     * @throws WrongInputException если входные данные некорректны
     * @throws WrongPasswordException если пароль некорректен
     * @throws LinkNotFoundException если ссылка не найдена
     */
    PageDTO getPageOfClicks(int page, int size, String shortLink, String password) throws WrongInputException, WrongPasswordException, LinkNotFoundException;

    /**
     * Отправляет статистику кликов на сервер.
     *
     * @param clickDTO объект DTO со статистикой кликов
     */
    void postStatistics(ClickDTO clickDTO);

    /**
     * Обновляет ссылку.
     *
     * @param shortLink короткая ссылка
     * @param fullLink полная ссылка
     * @throws LinkNotSecureException если ссылка не безопасна
     * @throws WrongInputException если входные данные некорректны
     * @throws FullLinkNotProvidedException если полная ссылка не была предоставлена
     * @throws FullLinkSizeException если размер полной ссылки превышает допустимый
     * @throws FullLinkFormatException если формат полной ссылки некорректен
     */
    void updateLink(String shortLink, String fullLink) throws LinkNotSecureException, WrongInputException,
            FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException;

    /**
     * Устанавливает пароль для ссылки.
     *
     * @param shortLink короткая ссылка
     * @param password пароль
     */
    void setPassword(String shortLink, String password);
}


