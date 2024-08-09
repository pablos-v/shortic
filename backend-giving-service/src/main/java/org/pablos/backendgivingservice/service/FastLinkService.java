package org.pablos.backendgivingservice.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.backendgivingservice.repository.FastLinkRepository;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.cache.annotation.CacheConfig;
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

    /**
     * TODO
     * @param shortLink сокращённая ссылка
     * @return
     * @throws LinkNotFoundException
     */
    @Override
    @Cacheable(
            cacheNames = "fullLink",
            key = "#shortLink"
    )
    public String getFullLink(final String shortLink) throws LinkNotFoundException{
        return getFastLink(shortLink).getFullLink();
    }

    @Override
    @Transactional
    public FastLinkDTO create(final FastLinkDTO fastLink) throws LinkProcessingException{
        CommonUtil.validateDTOShortLink(fastLink);
        if (repository.existsById(fastLink.getShortLink())) throw new LinkProcessingException(CommonUtil.EXISTS);

        FastLink response = repository.save(FastLinkMapper.toEntity(fastLink));
        return FastLinkMapper.toDTO(response);
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = "fullLink",
            key = "#fastLink.getShortLink()"
    )
    public FastLinkDTO update(final FastLinkDTO fastLink) throws LinkNotFoundException{
        CommonUtil.validateDTOShortLink(fastLink);
        repository.findById(fastLink.getShortLink()).orElseThrow(LinkNotFoundException::new);
        FastLink response = repository.save(FastLinkMapper.toEntity(fastLink));
        return FastLinkMapper.toDTO(response);
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = "fullLink",
            key = "#shortLink"
    )
    public FastLinkDTO deleteByShortLink(final String shortLink) throws LinkNotFoundException{
        FastLink fastLink = getFastLink(shortLink);
        repository.deleteById(shortLink);
        return FastLinkMapper.toDTO(fastLink);
    }

    private FastLink getFastLink(String shortLink) throws LinkNotFoundException{
        CommonUtil.validateShortLink(shortLink);
        return repository.findById(shortLink).orElseThrow(LinkNotFoundException::new);
    }

}
