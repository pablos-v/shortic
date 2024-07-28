package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.repository.LinkUnitRepository;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkUnitService {

    private final LinkUnitRepository linkUnitRepository;

    /**
     *
     * @param shortLink
     * @return
     * @throws LinkNotFoundException если ссылка не найдена
     */
    @Transactional(readOnly = true)
    public LinkUnitDTO getLinkUnitByShortLink(String shortLink) throws LinkNotFoundException{
        LinkUnit linkUnit = linkUnitRepository.findByShortLink(shortLink).orElseThrow(LinkNotFoundException::new);
        return LinkUnitMapper.toDto(linkUnit);
    }

}
