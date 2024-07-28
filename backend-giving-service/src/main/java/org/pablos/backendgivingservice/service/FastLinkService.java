package org.pablos.backendgivingservice.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.exception.ObjectNotProvidedException;
import org.pablos.backendgivingservice.repository.FastLinkRepository;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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

    @Override
    @Cacheable(
            value = "FastLinkService::getFullLink",
            key = "#shortLink"
    )
    public String getFullLink(final String shortLink){
        return getFastLink(shortLink).getFullLink();
    }

    @Override
    @Transactional
    @Cacheable(
            value = "FastLinkService::getFullLink",
            key = "#fastLink.shortLink()"
    )
    public FastLinkDTO create(final FastLinkDTO fastLink) {
        CommonUtil.validate(fastLink);
        if (repository.existsById(fastLink.getShortLink())) throw new LinkProcessingException(CommonUtil.EXISTS);

        FastLink response = repository.save(FastLinkMapper.toEntity(fastLink));
        return FastLinkMapper.toDTO(response);
    }

    @Override
    @Transactional
    @CachePut(
            value = "FastLinkService::getFullLink",
            key = "#fastLink.shortLink()"
    )
    public FastLinkDTO update(final FastLinkDTO fastLink) {
        CommonUtil.validate(fastLink);
        repository.findById(fastLink.getShortLink()).orElseThrow(LinkNotFoundException::new);
        FastLink response = repository.save(FastLinkMapper.toEntity(fastLink));
        return FastLinkMapper.toDTO(response);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "FastLinkService::getFullLink",
            key = "#shortLink"
    )
    public FastLinkDTO deleteByShortLink(final String shortLink) {
        FastLink fastLink = getFastLink(shortLink);
        repository.deleteById(shortLink);
        return FastLinkMapper.toDTO(fastLink);
    }

    private FastLink getFastLink(String shortLink) {
        CommonUtil.validate(new FastLinkDTO(shortLink,""));
        return repository.findById(shortLink).orElseThrow(LinkNotFoundException::new);
    }

}
