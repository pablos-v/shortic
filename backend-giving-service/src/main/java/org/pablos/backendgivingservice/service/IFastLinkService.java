package org.pablos.backendgivingservice.service;

import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.shortic.exception.LinkNotFoundException;

public interface IFastLinkService {

    /**
     * Кэшируемый метод получения ссылки. Предварительно валидирует ссылку.
     *
     * @param shortLink сокращённая ссылка
     * @return Полная ссылка
     * @throws LinkNotFoundException если ссылка не найдена
     */
    String getFullLink(final String shortLink);

    /**
     * Метод создания объекта {@link FastLink}. Предварительно валидирует ссылку.
     * Создаёт кэш.
     *
     * @param fastLink DTO, содержащий ссылки
     * @return {@link FastLinkDTO} DTO созданного объекта
     * @throws org.pablos.shortic.exception.LinkProcessingException если ссылка не валидна
     */
    FastLinkDTO create(final FastLinkDTO fastLink);

    /**
     * Метод обновления объекта {@link FastLink}. Предварительно валидирует ссылку.
     * Обновляет кэш.
     *
     * @param fastLink DTO, содержащий ссылки
     * @return {@link FastLinkDTO} DTO обновленного объекта
     * @throws LinkNotFoundException если ссылка не найдена
     */
    FastLinkDTO update(final FastLinkDTO fastLink) throws LinkNotFoundException;

    /**
     * Метод удаления объекта {@link FastLink} на основе сокращённой ссылки.
     * Стирает кэш для удалённого объекта.
     *
     * @param shortLink сокращённая ссылка
     * @return {@link FastLinkDTO} DTO удалённого объекта
     * @throws LinkNotFoundException если ссылка не найдена
     */
    FastLinkDTO deleteByShortLink(final String shortLink);
}
