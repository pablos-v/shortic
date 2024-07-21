package org.pablos.backendgivingservice.service;

import lombok.Data;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.backendgivingservice.domain.exception.LinkNotFoundException;
import org.pablos.backendgivingservice.repository.FastLinkRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO
 * стирание ссылки @Cacheable обнулять
 * запись ссылки @Cacheable вносить
 * изменение ссылки @Cacheable изменять
 * всё Transactional @Cacheable
 */
@Data
@Service
public class FastLinkService {

    private final FastLinkRepository repository;

    @Transactional(readOnly = true)
    @Cacheable(value = "FastLinkService::getFullLink", key = "#shortLink")
    public String getFullLink(String shortLink) {
        FastLink fastLink = repository.findById(shortLink).orElseThrow(LinkNotFoundException::new);
        return fastLink.getFullLink();
    }
}
