package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.shortic.exception.WrongPasswordException;
import org.pablos.backendcountingservice.repository.LinkUnitRepository;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkUnitService {

    private final LinkUnitRepository linkUnitRepository;
    private final IGivingServiceClient givingServiceClient;
    private final ILinkCheckingService checkingService;

    /**
     * @param shortLink
     * @return
     * @throws LinkNotFoundException если ссылка не найдена
     */
    @Transactional(readOnly = true)
    Long getLinkUnitIdByShortLink(final String shortLink) throws LinkNotFoundException {
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
        new Thread(() -> checkNewLinkSecurity(created)).start();
        
        return created;
    }

    /**
     * Проверяет безопасность новой ссылки. Активирует и сохраняет ее в БД.
     * Также запрашивает сохранение этой ссылки в backend-giving-service.
     * @param link
     * @throws LinkNotFoundWhileActivationException
     * @throws SavingFastLinkException
     * @throws DeletingFastLinkException
     */
    @Transactional
    void checkNewLinkSecurity(final FastLinkDTO link)
            throws LinkNotFoundWhileActivationException, SavingFastLinkException, DeletingFastLinkException {
        boolean linkIsOk = checkingService.checkLink(link);
        if (linkIsOk) {
            LinkUnit linkUnit = getLinkUnit(link);
            linkUnit.setActive(true);
            linkUnitRepository.save(linkUnit);

            givingServiceClient.saveFastLink(link);
        }
    }

    /**
     * Проверяет безопасность существующей ссылки. Деактивирует ссылку и сохраняет в БД.
     * Также запрашивает удаление этой ссылки из backend-giving-service.
     * @param link
     * @throws LinkNotFoundWhileActivationException
     * @throws SavingFastLinkException
     * @throws DeletingFastLinkException
     */
    @Transactional
    void checkExistingLinkSecurity(final FastLinkDTO link)
            throws LinkNotFoundWhileActivationException, SavingFastLinkException, DeletingFastLinkException {
        boolean linkIsOk = checkingService.checkLink(link);
        if (!linkIsOk) {
            LinkUnit linkUnit = getLinkUnit(link);
            linkUnit.setActive(false);
            linkUnitRepository.save(linkUnit);

            givingServiceClient.deleteFastLink(link);
        }
    }

    private LinkUnit getLinkUnit(final FastLinkDTO link) throws LinkNotFoundWhileActivationException {
        return linkUnitRepository.findByShortLink(link.getShortLink())
                .orElseThrow(LinkNotFoundWhileActivationException::new);
    }

    public LinkUnitDTO getLinkUnit(final LinkUnitDTO dto) throws LinkNotFoundException, WrongPasswordException {
        LinkUnit linkUnit = linkUnitRepository.findByShortLink(dto.getShortLink())
                .orElseThrow(LinkNotFoundException::new);

        if (!dto.getPassword().equals(linkUnit.getPassword())) {
            throw new WrongPasswordException();
        }
        return LinkUnitMapper.toDto(linkUnit);
    }

    @Transactional(readOnly = true)
    List<FastLinkDTO> getAllLinks() {
        return linkUnitRepository.findAll().stream()
                .map(linkUnit -> new FastLinkDTO(linkUnit.getShortLink(), linkUnit.getFullLink()))
                .toList();
    }
}
