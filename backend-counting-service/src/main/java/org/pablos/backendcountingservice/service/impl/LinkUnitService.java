package org.pablos.backendcountingservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.backendcountingservice.repository.LinkUnitRepository;
import org.pablos.backendcountingservice.service.IGivingServiceClient;
import org.pablos.backendcountingservice.service.ILinkCheckingService;
import org.pablos.backendcountingservice.service.ILinkUnitService;
import org.pablos.backendcountingservice.service.mapper.ClickMapper;
import org.pablos.backendcountingservice.service.mapper.LinkUnitMapper;
import org.pablos.common.dto.ClickDTO;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.dto.LinkUnitDTO;
import org.pablos.common.dto.PageDTO;
import org.pablos.common.exception.*;
import org.pablos.common.util.CommonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkUnitService implements ILinkUnitService {

    private final LinkUnitRepository linkUnitRepository;
    private final IGivingServiceClient givingServiceClient;
    private final ILinkCheckingService checkingService;

    @Override
    @Transactional(readOnly = true)
    public Long getLinkUnitIdByShortLink(final String shortLink) throws LinkNotFoundException {
        LinkUnit linkUnit = getLinkUnitByShortLink(shortLink);
        return linkUnit.getId();
    }

    @Override
    @Transactional
    public LinkUnitDTO createLinkUnit(final FastLinkDTO input) throws LinkNotFoundWhileActivationException, FullLinkSizeException, FullLinkFormatException, FullLinkNotProvidedException {
        CommonUtil.validateDTOFullLink(input);
        String shortLink;
        do {
            shortLink = CommonUtil.generateShortLink();
            // проверка на уникальность
        } while (linkUnitRepository.findByShortLink(shortLink).isPresent());

        LinkUnit saved = linkUnitRepository.save(new LinkUnit(shortLink, input.getFullLink()));

        // отправит ссылку на проверку и присвоит ей соответствующий статус
        new Thread(() -> checkNewLinkSecurity(LinkUnitMapper.toDto(saved))).start();

        return LinkUnitMapper.toDto(saved);
    }

    @Override
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
        return new PageDTO (pageClicks.getContent(), pageClicks.getTotalPages(), LinkUnitMapper.toDto(linkUnit), linkUnit.getClicks().size());
    }

    private Page<ClickDTO> getPageOfClicks(List<Click> clicks, Pageable paging) {
        // Получаем подсписок в зависимости от параметров пагинации
        int start = (int) paging.getOffset();
        int end = Math.min(start + paging.getPageSize(), clicks.size());

        List<ClickDTO> pagedList = clicks.stream()
                .map(ClickMapper::toDTO)
                .sorted(Comparator.comparing(ClickDTO::getClickTime))
                .toList()
                .subList(start, end);

        return new PageImpl<>(pagedList, paging, clicks.size());
    }

    @Override
    @Transactional
    public void checkNewLinkSecurity(final LinkUnitDTO dto)
            throws LinkNotFoundWhileActivationException, SavingFastLinkException, DeletingFastLinkException {
        boolean linkIsOk = checkingService.checkLink(dto.getFullLink());
        if (linkIsOk) {
            LinkUnit linkUnit = getLinkUnitByShortLink(dto.getShortLink());
            linkUnit.setActive(true);
            linkUnitRepository.save(linkUnit);

            givingServiceClient.saveFastLink(new FastLinkDTO(dto.getShortLink(), dto.getFullLink()));
        }
    }

    @Override
    @Transactional
    public boolean checkExistingLinkSecurity(final LinkUnitDTO dto)
            throws LinkNotFoundWhileActivationException, SavingFastLinkException, DeletingFastLinkException {
        boolean linkIsOk = checkingService.checkLink(dto.getFullLink());
        if (!linkIsOk) {
            LinkUnit linkUnit = getLinkUnitByShortLink(dto.getShortLink());
            linkUnit.setActive(false);
            linkUnitRepository.save(linkUnit);

            givingServiceClient.deleteFastLink(new FastLinkDTO(dto.getShortLink(), dto.getFullLink()));
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkUnit> getAllLinks() {
        return linkUnitRepository.findAll();
    }

    @Override
    @Transactional
    public void updateLinkUnit(final String shortLink, final String fullLink) throws LinkNotSecureException,
            LinkNotFoundException, FullLinkNotProvidedException, FullLinkFormatException, FullLinkSizeException {

        CommonUtil.validateFullLink(fullLink);
        LinkUnit linkUnit = getLinkUnitByShortLink(shortLink);
        linkUnit.setFullLink(fullLink);
        boolean secure = checkExistingLinkSecurity(LinkUnitMapper.toDto(linkUnit));
        if (!secure) {
            return;
        }
        linkUnitRepository.save(linkUnit);
        givingServiceClient.updateFastLink(new FastLinkDTO(linkUnit.getShortLink(), linkUnit.getFullLink()));
    }

    @Override
    @Transactional
    public void setPassword(final String shortLink, final String password)
            throws WrongInputException, LinkNotFoundException {

        CommonUtil.validatePassword(CommonUtil.decodePassword(password));
        LinkUnit linkUnit = getLinkUnitByShortLink(shortLink);
        linkUnit.setPassword(password);
        linkUnitRepository.save(linkUnit);
    }
}
