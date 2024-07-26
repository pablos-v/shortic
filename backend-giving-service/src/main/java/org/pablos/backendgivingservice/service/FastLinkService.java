package org.pablos.backendgivingservice.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.FastLinkDTO;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.backendgivingservice.domain.exception.LinkNotFoundException;
import org.pablos.backendgivingservice.domain.exception.LinkProcessingException;
import org.pablos.backendgivingservice.domain.exception.ObjectNotProvidedException;
import org.pablos.backendgivingservice.repository.FastLinkRepository;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String NOT_PROVIDED = "Link was not provided";
    private static final String BAD_SIZE = "Link length is wrong";
    private static final String CONTAINS_SPACES = "Link contains spaces";
    private static final String INVALID_CHARS = "Link contains invalid characters";
    private static final String EXISTS = "This short link already exists";

    @Value("${properties.shortLinkLength}")
    private int shortLinkLength;

    private final FastLinkRepository repository;

    @Override
    @Cacheable(
            value = "FastLinkService::getFullLink",
            key = "#shortLink"
    )
    public String getFullLink(final String shortLink) {
        validateShortLink(shortLink);
        FastLink fastLink = repository.findById(shortLink).orElseThrow(LinkNotFoundException::new);
        return fastLink.getFullLink();
    }

    @Override
    @Transactional
    @Cacheable(
            value = "FastLinkService::getFullLink",
            key = "#fastLink.shortLink()"
    )
    public FastLinkDTO create(final FastLinkDTO fastLink) {
        if (fastLink == null) throw new ObjectNotProvidedException();
        validateShortLink(fastLink.shortLink());
        if (repository.existsById(fastLink.shortLink())) throw new LinkProcessingException(EXISTS);

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
        if (fastLink == null) throw new ObjectNotProvidedException();
        validateShortLink(fastLink.shortLink());
        repository.findById(fastLink.shortLink()).orElseThrow(LinkNotFoundException::new);
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
        FastLink fastLink = repository.findById(shortLink).orElseThrow(LinkNotFoundException::new);
        repository.deleteById(shortLink);
        return FastLinkMapper.toDTO(fastLink);
    }

    private void validateShortLink(final String link) {
        if (link == null || link.isEmpty()) {
            throw new LinkProcessingException(NOT_PROVIDED);
        }
        if (link.length() != shortLinkLength) {
            throw new LinkProcessingException(BAD_SIZE);
        }
        if (link.contains(" ")) {
            throw new LinkProcessingException(CONTAINS_SPACES);
        }
        if (!link.matches("^[A-Za-z0-9]+$")) {
            throw new LinkProcessingException(INVALID_CHARS);
        }
    }
}
