package org.pablos.backendcountingservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.backendcountingservice.service.ILinkUnitService;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.dto.LinkUnitDTO;
import org.pablos.common.dto.PageDTO;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LinkUnitControllerTest {

    private final FastLinkDTO fastLinkDTO = new FastLinkDTO();
    private final LinkUnitDTO linkUnitDTO = new LinkUnitDTO();
    private final PageDTO pageDTO = new PageDTO(new ArrayList<>(),0,new LinkUnitDTO(),0);
    @Mock
    private ILinkUnitService linkUnitService;

    @InjectMocks
    private LinkUnitController linkUnitController;

    @Test
    public void testCreateLinkUnit() {
        when(linkUnitService.createLinkUnit(fastLinkDTO)).thenReturn(linkUnitDTO);

        ResponseEntity<LinkUnitDTO> responseEntity = linkUnitController.createLinkUnit(fastLinkDTO);

        verify(linkUnitService, times(1)).createLinkUnit(fastLinkDTO);
        assertThat(responseEntity).isEqualTo(ResponseEntity.ok(linkUnitDTO));
    }

    @Test
    public void testUpdateFullLinkInLinkUnit() {
        String shortLink = "shortLink";
        String fullLink = "fullLink";

        ResponseEntity<Void> responseEntity = linkUnitController.updateFullLinkInLinkUnit(shortLink, fullLink);

        verify(linkUnitService, times(1)).updateLinkUnit(shortLink, fullLink);
        assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build());
    }

    @Test
    public void testSetPassword() {
        String shortLink = "shortLink";
        String password = "password";

        ResponseEntity<Void> responseEntity = linkUnitController.setPassword(shortLink, password);

        verify(linkUnitService, times(1)).setPassword(shortLink, password);
        assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build());
    }

}


