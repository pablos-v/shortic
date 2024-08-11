package org.pablos.backendgivingservice.service;

import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.exception.ObjectNotProvidedException;

public interface IFastLinkService {

    /**
     * Кэшируемый метод получения ссылки. В процессе валидирует ссылку.
     *
     * @param shortLink сокращённая ссылка
     * @return Полная ссылка
     * @throws ObjectNotProvidedException если объект не был передан
     * @throws LinkProcessingException если ссылка не валидна
     * @throws LinkNotFoundException если ссылка не найдена
     */
    String getFullLink(final String shortLink) throws LinkNotFoundException, LinkProcessingException, ObjectNotProvidedException;

    /**
     * Метод создания и записи в БД объекта {@link FastLink}. В процессе валидирует ссылку.
     * Создаёт кэш.
     *
     * @param fastLink DTO, содержащий ссылки
     * @return {@link FastLinkDTO} DTO созданного объекта
     * @throws ObjectNotProvidedException если объект не был передан
     * @throws LinkProcessingException если ссылка не валидна
     */
    FastLinkDTO create(final FastLinkDTO fastLink) throws LinkNotFoundException, LinkProcessingException;

    /**
     * Метод обновления полной ссылки и записи в БД объекта {@link FastLink}. В процессе валидирует ссылку.
     * Обновляет кэш.
     *
     * @param fastLink DTO, содержащий ссылки
     * @return {@link FastLinkDTO} DTO обновленного объекта
     * @throws ObjectNotProvidedException если объект не был передан
     * @throws LinkProcessingException если ссылка не валидна
     * @throws LinkNotFoundException если ссылка не найдена
     */
    FastLinkDTO update(final FastLinkDTO fastLink) throws LinkProcessingException, LinkNotFoundException, ObjectNotProvidedException;

    /**
     * Метод удаления из БД объекта {@link FastLink} на основе сокращённой ссылки.
     * Стирает кэш для удалённого объекта.
     *
     * @param shortLink сокращённая ссылка
     * @return {@link FastLinkDTO} DTO удалённого объекта
     * @throws LinkNotFoundException если ссылка не найдена
     */
    FastLinkDTO deleteByShortLink(final String shortLink) throws LinkNotFoundException;
}
