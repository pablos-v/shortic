package org.pablos.backendgivingservice.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.backendgivingservice.entity.FastLink;
import org.pablos.backendgivingservice.repository.FastLinkRepository;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.exception.*;
import org.pablos.common.util.CommonUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы с объектами ссылок
 */
@Data
@Service
@RequiredArgsConstructor
public class FastLinkService implements IFastLinkService {

    private final FastLinkRepository repository;

    /**
     * Возвращает полную ссылку по короткой ссылке.
     * При наличии в Кэше - возвращает из Кэша, иначе - возвращает и записывает в Кэш.
     * @param shortLink короткая ссылка
     * @return полная ссылка
     * @throws LinkNotFoundException если ссылка не найдена
     * @throws LinkProcessingException если произошла ошибка при обработке ссылки
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "fullLink",
            key = "#shortLink",
            unless = "#result == null"
    )
    public String getFullLink(final String shortLink) throws LinkNotFoundException, LinkProcessingException {
        return getFastLink(shortLink).getFullLink();
    }

    /**
     * Создаёт объект {@link FastLink} на основе переданного DTO
     * @param fastLink объект DTO, содержащий короткую и полную ссылки
     * @return {@link FastLinkDTO} DTO созданного объекта
     * @throws ObjectNotProvidedException если DTO не был предоставлен
     * @throws LinkProcessingException если произошла ошибка при обработке ссылки
     * @throws FullLinkNotProvidedException если полная ссылка не была предоставлена
     * @throws FullLinkSizeException если размер полной ссылки превышает допустимый
     * @throws FullLinkFormatException если формат полной ссылки некорректен
     */
    @Override
    @Transactional
    public FastLinkDTO create(final FastLinkDTO fastLink) throws ObjectNotProvidedException, LinkProcessingException, FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException {
        CommonUtil.validateDTOShortLink(fastLink);
        CommonUtil.validateDTOFullLink(fastLink);
        if (repository.existsById(fastLink.getShortLink())) throw new LinkProcessingException(CommonUtil.EXISTS);

        return FastLinkMapper.toDTO(repository.save(FastLinkMapper.toEntity(fastLink)));
    }

    /**
     * Обновляет объект {@link FastLink} на основе переданного DTO, и также обновляет его в Кэше
     * @param dto объект DTO, содержащий короткую и обновлённую полную ссылки
     * @return {@link FastLinkDTO} DTO измененного объекта
     * @throws LinkNotFoundException если ссылка не найдена
     * @throws ObjectNotProvidedException если DTO не был предоставлен
     * @throws LinkProcessingException если произошла ошибка при обработке ссылки
     * @throws FullLinkNotProvidedException если полная ссылка не была предоставлена
     * @throws FullLinkSizeException если размер полной ссылки превышает допустимый
     * @throws FullLinkFormatException если формат полной ссылки некорректен
     */
    @Override
    @Transactional
    @CacheEvict(
            cacheNames = "fullLink",
            key = "#dto.getShortLink()"
    )
    public FastLinkDTO update(final FastLinkDTO dto) throws LinkNotFoundException, ObjectNotProvidedException,
            LinkProcessingException, FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException {
        CommonUtil.validateDTOShortLink(dto);
        CommonUtil.validateDTOFullLink(dto);
        FastLink fastLink = getFastLink(dto.getShortLink());
        fastLink.setFullLink(dto.getFullLink());
        return FastLinkMapper.toDTO(repository.save(fastLink));
    }

    /**
     * Удаляет объект {@link FastLink} из БД и из Кэша.
     * @param shortLink Короткая ссылка, она же ID удаляемого объекта {@link FastLink}
     * @return {@link FastLinkDTO} DTO удалённого объекта
     * @throws LinkNotFoundException если ссылка не найдена
     * @throws LinkProcessingException если произошла ошибка при обработке ссылки
     */
    @Override
    @Transactional
    @CacheEvict(
            cacheNames = "fullLink",
            key = "#shortLink"
    )
    public FastLinkDTO deleteByShortLink(final String shortLink) throws LinkNotFoundException, LinkProcessingException {
        FastLink fastLink = getFastLink(shortLink);
        repository.deleteById(shortLink);
        return FastLinkMapper.toDTO(fastLink);
    }

    /**
     * Возвращает объект {@link FastLink} по короткой ссылке
     * @param shortLink короткая ссылка
     * @return объект {@link FastLink}
     * @throws LinkNotFoundException если ссылка не найдена
     * @throws LinkProcessingException если произошла ошибка при обработке ссылки
     */
    private FastLink getFastLink(String shortLink) throws LinkNotFoundException, LinkProcessingException {
        CommonUtil.validateShortLink(shortLink);
        return repository.findById(shortLink).orElseThrow(LinkNotFoundException::new);
    }

}


