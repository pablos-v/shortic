package org.pablos.backendgivingservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.backendgivingservice.entity.FastLink;
import org.pablos.backendgivingservice.repository.FastLinkRepository;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.exception.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FastLinkServiceTest {

    public static final String SHORT_LINK = "shortL";
    @Mock
    private FastLinkRepository repository;

    @InjectMocks
    private FastLinkService fastLinkService;

    @Test
    public void testUpdateWithObjectNotProvidedException() {
        FastLinkDTO dto = null;

        assertThrows(ObjectNotProvidedException.class, () -> fastLinkService.update(dto));
        verify(repository, never()).findById(anyString());
        verify(repository, never()).save(any(FastLink.class));
    }
}
