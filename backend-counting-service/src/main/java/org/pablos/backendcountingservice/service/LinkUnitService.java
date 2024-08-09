package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.shortic.dto.*;
import org.pablos.shortic.exception.LinkNotSecureException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.shortic.exception.WrongPasswordException;
import org.pablos.backendcountingservice.repository.LinkUnitRepository;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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
        LinkUnit linkUnit = getLinkUnitByShortLink(shortLink);
        return linkUnit.getId();
    }

    @Transactional
    public LinkUnitDTO createLinkUnit(final FastLinkDTO input) throws LinkNotFoundWhileActivationException{
        String shortLink;
        do {
            shortLink = CommonUtil.generateShortLink();
            // проверка на уникальность
        } while (linkUnitRepository.findByShortLink(shortLink).isPresent());

        LinkUnit saved = linkUnitRepository.save(new LinkUnit(shortLink, input.getFullLink()));

        // отправит ссылку на проверку и присвоит ей соответствующий статус
        new Thread(() -> checkNewLinkSecurity(saved)).start();

        return LinkUnitMapper.toDto(saved);
    }

    /**
     * Меняет полную ссылку у существующего LinkUnit. Проверяет безопасность новой ссылки.
     * Формирует на основе PageRequestDTO страницу для пагинации.
     * @param dto
     * @return
     * @throws LinkNotSecureException
     */
    @Transactional
    public PageDTO updateLinkUnitAndGetPage(final PageRequestDTO dto) throws LinkNotSecureException, LinkNotFoundException {
        LinkUnit linkUnit = getLinkUnitByShortLink(dto.getLinkUnit().getShortLink());
        linkUnit.setFullLink(dto.getLinkUnit().getFullLink());
        boolean secure = checkExistingLinkSecurity(linkUnit);
        if (!secure) {
            throw new LinkNotSecureException();
        }
        return createPage(dto.getPage(), dto.getSize(), linkUnitRepository.save(linkUnit));
    }

    /**
     * Получает из репозитория LinkUnit и формирует на основе PageRequestDTO страницу для пагинации.
     * @param dto
     * @return
     * @throws LinkNotFoundException
     * @throws WrongPasswordException
     */
    public PageDTO getPage(int page, int size, String shortLink, String password) throws LinkNotFoundException, WrongPasswordException {
        LinkUnit linkUnit = getLinkUnitByShortLink(shortLink);

        if (!password.equals(linkUnit.getPassword())) {
            throw new WrongPasswordException();
        }
        return createPage(page, size, linkUnit);
    }

    private LinkUnit getLinkUnitByShortLink(String shortLink) throws LinkNotFoundException {
        return linkUnitRepository.findByShortLink(shortLink).orElseThrow(LinkNotFoundException::new);
    }

    private PageDTO createPage(int page, int size, LinkUnit linkUnit) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<ClickDTO> pageClicks = getPageOfClicks(linkUnit.getClicks(), paging);
        return new PageDTO (pageClicks.getContent(), pageClicks.getTotalPages(), LinkUnitMapper.toDto(linkUnit));
    }

    private Page<ClickDTO> getPageOfClicks(List<Click> clicks, Pageable paging) {
        // Получаем подсписок в зависимости от параметров пагинации
        int start = (int) paging.getOffset();
        int end = Math.min(start + paging.getPageSize(), clicks.size());

//        ArrayList<ClickDTO> DTOclicks = new ArrayList<>(ClickMapper.toDTOList(clicks));
//        DTOclicks.sort(Comparator.comparing(ClickDTO::getClickTime));

        List<ClickDTO> pagedList = clicks.stream()
                .map(ClickMapper::toDTO)
                .sorted(Comparator.comparing(ClickDTO::getClickTime))
                .toList()
                .subList(start, end);

        return new PageImpl<>(pagedList, paging, clicks.size());
    }

    /**
     * Проверяет безопасность новой ссылки. Активирует и сохраняет ее в БД.
     * Также запрашивает сохранение этой ссылки в backend-giving-service.
     * @param linkUnit
     * @throws LinkNotFoundWhileActivationException
     * @throws SavingFastLinkException
     * @throws DeletingFastLinkException
     */
    @Transactional
    void checkNewLinkSecurity(final LinkUnit linkUnit)
            throws LinkNotFoundWhileActivationException, SavingFastLinkException, DeletingFastLinkException {
        boolean linkIsOk = checkingService.checkLink(linkUnit.getFullLink());
        if (linkIsOk) {
            linkUnit.setActive(true);
            linkUnitRepository.save(linkUnit);

            givingServiceClient.saveFastLink(new FastLinkDTO(linkUnit.getShortLink(), linkUnit.getFullLink()));
        }
    }

    /**
     * Проверяет безопасность существующей ссылки. Деактивирует ссылку и сохраняет в БД.
     * Также запрашивает удаление этой ссылки из backend-giving-service.
     * @param linkUnit
     * @throws LinkNotFoundWhileActivationException
     * @throws SavingFastLinkException
     * @throws DeletingFastLinkException
     */
    @Transactional
    boolean checkExistingLinkSecurity(final LinkUnit linkUnit)
            throws LinkNotFoundWhileActivationException, SavingFastLinkException, DeletingFastLinkException {
        boolean linkIsOk = checkingService.checkLink(linkUnit.getFullLink());
        if (!linkIsOk) {
            linkUnit.setActive(false);
            linkUnitRepository.save(linkUnit);

            givingServiceClient.deleteFastLink(new FastLinkDTO(linkUnit.getShortLink(), linkUnit.getFullLink()));
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    List<LinkUnit> getAllLinks() {
        return linkUnitRepository.findAll();
    }

}
