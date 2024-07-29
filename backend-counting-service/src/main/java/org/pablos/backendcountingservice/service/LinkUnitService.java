package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.repository.LinkUnitRepository;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkUnitService {

    private final LinkUnitRepository linkUnitRepository;

    /**
     * @param shortLink
     * @return
     * @throws LinkNotFoundException если ссылка не найдена
     */
    @Transactional(readOnly = true)
    public Long getLinkUnitIdByShortLink(final String shortLink) throws LinkNotFoundException {
        LinkUnit linkUnit = linkUnitRepository.findByShortLink(shortLink).orElseThrow(LinkNotFoundException::new);
        return linkUnit.getId();
    }

    @Transactional
    public FastLinkDTO createLinkUnit(final FastLinkDTO input) {
        String shortLink;
        do {
            shortLink = CommonUtil.generateShortLink();
            // проверка на уникальность
        } while (linkUnitRepository.findByShortLink(shortLink).isPresent());

        LinkUnit linkUnit = new LinkUnit(shortLink, input.getFullLink());
        linkUnitRepository.save(linkUnit);
        return new FastLinkDTO(shortLink, input.getFullLink());
    }
}
