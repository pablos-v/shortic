package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.dto.ApiRequestBody;
import org.pablos.backendcountingservice.domain.dto.Match;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.repository.LinkUnitRepository;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkUnitService {

    private final RestTemplate restTemplate;
    private final LinkUnitRepository linkUnitRepository;
    private final IGivingServiceClient givingServiceClient;
    private final ILinkCheckingService checkingService;

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
    public FastLinkDTO createLinkUnit(final FastLinkDTO input) throws LinkNotFoundWhileActivationException{
        String shortLink;
        do {
            shortLink = CommonUtil.generateShortLink();
            // проверка на уникальность
        } while (linkUnitRepository.findByShortLink(shortLink).isPresent());

        LinkUnit linkUnit = new LinkUnit(shortLink, input.getFullLink());
        
        linkUnitRepository.save(linkUnit);
        
        FastLinkDTO created = new FastLinkDTO(shortLink, input.getFullLink());
        
        // отправит ссылку на проверку и присвоит ей соответствующий статус
        new Thread(() -> checkLinkSecurity(created)).start();
        
        return created;
    }

    private void checkLinkSecurity(FastLinkDTO link) {
        boolean linkIsOk = checkingService.checkLink(link);
        if (linkIsOk) {
            activateLink(link);
        } else {
            deactivateLink(link);
        }
    }

    private void activateLink(FastLinkDTO link) {
        LinkUnit linkUnit = getLinkUnit(link);
        linkUnit.setActive(true);
        linkUnitRepository.save(linkUnit);

        givingServiceClient.saveFastLink(link);
    }

    private void deactivateLink(FastLinkDTO link) {
        LinkUnit linkUnit = getLinkUnit(link);
        linkUnit.setActive(false);
        linkUnitRepository.save(linkUnit);

        givingServiceClient.deleteFastLink(link);
    }

    private LinkUnit getLinkUnit(FastLinkDTO link) {
        return linkUnitRepository.findByShortLink(link.getShortLink())
                .orElseThrow(LinkNotFoundWhileActivationException::new);
    }
}
