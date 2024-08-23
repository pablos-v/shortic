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

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "fullLink",
            key = "#shortLink"
    )
    public String getFullLink(final String shortLink) {
        return getFastLink(shortLink).getFullLink();
    }

    @Override
    @Transactional
    public FastLinkDTO create(final FastLinkDTO fastLink) throws ObjectNotProvidedException, LinkProcessingException,
            FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException {
        CommonUtil.validateDTOShortLink(fastLink);
        CommonUtil.validateDTOFullLink(fastLink);
        if (repository.existsById(fastLink.getShortLink())) throw new LinkProcessingException(CommonUtil.EXISTS);

        return FastLinkMapper.toDTO(repository.save(FastLinkMapper.toEntity(fastLink)));
    }

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

    private FastLink getFastLink(String shortLink) throws LinkNotFoundException, LinkProcessingException {
        CommonUtil.validateShortLink(shortLink);
        return repository.findById(shortLink).orElseThrow(LinkNotFoundException::new);
    }

}
