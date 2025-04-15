package org.pablos.backendgivingservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.backendgivingservice.controller.FastLinkController;
import org.pablos.backendgivingservice.service.IFastLinkService;
import org.pablos.common.dto.FastLinkDTO;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FastLinkControllerTest {

    @Mock
    private IFastLinkService service;

    @InjectMocks
    private FastLinkController controller;

    @Test
    public void testGetFullLink() {
        String shortLink = "shortLink";
        String fullLink = "fullLink";
        when(service.getFullLink(shortLink)).thenReturn(fullLink);

        ResponseEntity<String> responseEntity = controller.getFullLink(shortLink);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(fullLink);
        verify(service, times(1)).getFullLink(shortLink);
    }

    @Test
    public void testCreateFastLink() {
        FastLinkDTO dto = new FastLinkDTO();
        FastLinkDTO createdDto = new FastLinkDTO();
        when(service.create(dto)).thenReturn(createdDto);

        ResponseEntity<FastLinkDTO> responseEntity = controller.createFastLink(dto);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(createdDto);
        verify(service, times(1)).create(dto);
    }

    @Test
    public void testCreateFastLinkWithNullDto() {
        FastLinkDTO dto = null;

        ResponseEntity<FastLinkDTO> responseEntity = controller.createFastLink(dto);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNull();
        verify(service, never()).create(any(FastLinkDTO.class));
    }

    @Test
    public void testUpdateFastLink() {
        FastLinkDTO dto = new FastLinkDTO();
        FastLinkDTO updatedDto = new FastLinkDTO();
        when(service.update(dto)).thenReturn(updatedDto);

        ResponseEntity<FastLinkDTO> responseEntity = controller.updateFastLink(dto);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(updatedDto);
        verify(service, times(1)).update(dto);
    }

    @Test
    public void testUpdateFastLinkWithNullDto() {
        FastLinkDTO dto = null;

        ResponseEntity<FastLinkDTO> responseEntity = controller.updateFastLink(dto);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNull();
        verify(service, never()).update(any(FastLinkDTO.class));
    }

    @Test
    public void testDeleteFastLink() {
        String shortLink = "shortLink";
        FastLinkDTO deletedDto = new FastLinkDTO();
        when(service.deleteByShortLink(shortLink)).thenReturn(deletedDto);

        ResponseEntity<FastLinkDTO> responseEntity = controller.deleteFastLink(shortLink);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(deletedDto);
        verify(service, times(1)).deleteByShortLink(shortLink);
    }

    @Test
    public void testDeleteFastLinkWithNullShortLink() {
        String shortLink = null;

        ResponseEntity<FastLinkDTO> responseEntity = controller.deleteFastLink(shortLink);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNull();
        verify(service, never()).deleteByShortLink(anyString());
    }

}



