package org.pablos.backendcountingservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.backendcountingservice.repository.LinkUnitRepository;
import org.pablos.backendcountingservice.service.IGivingServiceClient;
import org.pablos.backendcountingservice.service.ILinkCheckingService;
import org.pablos.backendcountingservice.service.ILinkUnitService;
import org.pablos.backendcountingservice.service.impl.LinkUnitService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LinkUnitServiceTest {

    @Mock
    private LinkUnitRepository linkUnitRepository;

    @Mock
    private IGivingServiceClient givingServiceClient;

    @Mock
    private ILinkCheckingService checkingService;

    @InjectMocks
    private LinkUnitService linkUnitService;

    @Test
    public void testGetLinkUnitIdByShortLink() throws LinkNotFoundException {
        String shortLink = "shortLink";
        LinkUnit linkUnit = new LinkUnit(shortLink, "fullLink");
        when(linkUnitRepository.findByShortLink(shortLink)).thenReturn(Optional.of(linkUnit));

        Long result = linkUnitService.getLinkUnitIdByShortLink(shortLink);

        assertThat(result).isEqualTo(linkUnit.getId());
        verify(linkUnitRepository, times(1)).findByShortLink(shortLink);
    }

    @Test
    public void testGetLinkUnitIdByShortLinkWithLinkNotFoundException() {
        String shortLink = "shortLink";
        when(linkUnitRepository.findByShortLink(shortLink)).thenReturn(Optional.empty());

        assertThrows(LinkNotFoundException.class, () -> linkUnitService.getLinkUnitIdByShortLink(shortLink));
        verify(linkUnitRepository, times(1)).findByShortLink(shortLink);
    }

    @Test
    public void testGetPageWithLinkNotFoundException() throws LinkNotFoundException, WrongPasswordException {
        int page = 1;
        int size = 10;
        String shortLink = "shortLink";
        String password = "password";
        when(linkUnitRepository.findByShortLink(shortLink)).thenReturn(Optional.empty());

        assertThrows(LinkNotFoundException.class, () -> linkUnitService.getPage(page, size, shortLink, password));
        verify(linkUnitRepository, times(1)).findByShortLink(shortLink);
    }

    @Test
    public void testGetPageWithWrongPasswordException() throws LinkNotFoundException, WrongPasswordException {
        int page = 1;
        int size = 10;
        String shortLink = "shortLink";
        String password = "password";
        LinkUnit linkUnit = new LinkUnit(shortLink, "fullLink");
        linkUnit.setPassword("wrongPassword");
        when(linkUnitRepository.findByShortLink(shortLink)).thenReturn(Optional.of(linkUnit));

        assertThrows(WrongPasswordException.class, () -> linkUnitService.getPage(page, size, shortLink, password));
        verify(linkUnitRepository, times(1)).findByShortLink(shortLink);
    }

    @Test
    public void testCheckNewLinkSecurityWithLinkNotFoundWhileActivationException() throws LinkNotFoundWhileActivationException, SavingFastLinkException, DeletingFastLinkException {
        LinkUnitDTO dto = new LinkUnitDTO();
        boolean linkIsOk = true;
        when(checkingService.checkLink(dto.getFullLink())).thenReturn(linkIsOk);
        doThrow(LinkNotFoundWhileActivationException.class).when(linkUnitRepository).findByShortLink(dto.getShortLink());

        assertThrows(LinkNotFoundWhileActivationException.class, () -> linkUnitService.checkNewLinkSecurity(dto));
        verify(checkingService, times(1)).checkLink(dto.getFullLink());
        verify(linkUnitRepository, times(1)).findByShortLink(dto.getShortLink());
    }

}