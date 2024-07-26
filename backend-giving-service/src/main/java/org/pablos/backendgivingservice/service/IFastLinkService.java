package org.pablos.backendgivingservice.service;

import org.pablos.FastLinkDTO;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

public interface IFastLinkService {

    /**
     * Кэшируемый метод получения ссылки. Предварительно валидирует ссылку.
     *
     * @param shortLink сокращённая ссылка
     * @return Полная ссылка
     */
    String getFullLink(final String shortLink);

    /**
     * Метод создания объекта {@link FastLink}. Предварительно валидирует ссылку.
     * Создаёт кэш.
     *
     * @param fastLink DTO, содержащий ссылки
     * @return {@link FastLinkDTO} DTO созданного объекта
     */
    FastLinkDTO create(final FastLinkDTO fastLink);

    /**
     * Метод обновления объекта {@link FastLink}. Предварительно валидирует ссылку.
     * Обновляет кэш.
     *
     * @param fastLink DTO, содержащий ссылки
     * @return {@link FastLinkDTO} DTO обновленного объекта
     */

    FastLinkDTO update(final FastLinkDTO fastLink);

    /**
     * Метод удаления объекта {@link FastLink} на основе сокращённой ссылки.
     * Стирает кэш для удалённого объекта.
     *
     * @param shortLink сокращённая ссылка
     * @return {@link FastLinkDTO} DTO удалённого объекта
     */

    FastLinkDTO deleteByShortLink(final String shortLink);
}
