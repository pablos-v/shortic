package org.pablos.backendcountingservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.backendcountingservice.repository.ClickRepository;
import org.pablos.backendcountingservice.service.ILinkUnitService;
import org.pablos.common.dto.ClickDTO;
import org.pablos.common.exception.LinkNotFoundException;
import org.pablos.common.exception.LinkProcessingException;
import org.pablos.common.exception.ObjectNotProvidedException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ClickServiceTest {

    @Mock
    private ClickRepository clickRepository;

    @Mock
    private ILinkUnitService linkUnitService;

    @InjectMocks
    private ClickService clickService;

    @Test
    public void testCreateClickWithNullDTO() {
        ClickDTO dto = null;

        try {
            clickService.createClick(dto);
        } catch (ObjectNotProvidedException e) {
            assertThat(e.getMessage()).isEqualTo("Object was not provided");
        } catch (LinkNotFoundException | LinkProcessingException e) {
            // do nothing
        }
    }

    @Test
    public void testCreateClickWithInvalidShortLink() {
        ClickDTO dto = new ClickDTO(-1,"invalidShortLink",null,null,null,null, null);

        try {
            clickService.createClick(dto);
        } catch (LinkProcessingException e) {
            assertThat(e.getMessage()).isEqualTo("Link length is wrong");
        } catch (ObjectNotProvidedException | LinkNotFoundException e) {
            // do nothing
        }
    }

    @Test
    public void testCreateClickWithNonexistentShortLink() {
        ClickDTO dto = new ClickDTO(-1,"nonexistentShortLink",null,null,null,null, null);

        try {
            clickService.createClick(dto);
        } catch (LinkNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Link not found");
        } catch (ObjectNotProvidedException | LinkProcessingException e) {
            // do nothing
        }
    }

}



