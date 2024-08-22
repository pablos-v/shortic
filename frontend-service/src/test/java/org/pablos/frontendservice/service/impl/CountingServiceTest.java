package org.pablos.frontendservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.shortic.dto.ClickDTO;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.dto.PageDTO;
import org.pablos.shortic.exception.*;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountingServiceTest {

    public static final String SHORT_LINK = "shortL";
    public static final String FULL_LINK = "http://ya.ru";
    public static final String PASSWORD = "12345";
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CountingService countingService;

    private final FastLinkDTO input = new FastLinkDTO(SHORT_LINK, FULL_LINK);

    @Test
    public void testCreateLinkSuccess() throws WrongInputException, FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException {
        LinkUnitDTO expectedOutput = new LinkUnitDTO();
        when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>(expectedOutput, HttpStatus.OK));

        LinkUnitDTO actualOutput = countingService.createLink(input);

        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    @Test
    public void testCreateLinkGettingStatus310() {
        when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT));

        assertThatThrownBy(() -> countingService.createLink(input))
                .isNotNull()
                .isInstanceOf(WrongInputException.class)
                .hasMessageContaining("Unknown error");
    }

    @Test
    public void testCreateLinkHttpClientErrorException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", null,
                "Test Exception".getBytes(), null);
        when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenThrow(exception);

        assertThatThrownBy(() -> countingService.createLink(input))
                .isNotNull()
                .isInstanceOf(WrongInputException.class)
                .hasMessageContaining("Test Exception");
    }

    @Test
    public void testGetPageOfClicksSuccess() throws WrongInputException, WrongPasswordException, LinkNotFoundException {
        LinkUnitDTO linkUnit = new LinkUnitDTO();
        linkUnit.setPassword(CommonUtil.encodePassword(PASSWORD));
        PageDTO expectedOutput = new PageDTO(null, 0, linkUnit);
        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(expectedOutput, HttpStatus.OK));

        PageDTO actualOutput = countingService.getPageOfClicks(0, 10, SHORT_LINK, PASSWORD);

        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    @Test
    public void testGetPageOfClicksWrongPasswordException() {
        when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        assertThatThrownBy(() -> countingService.getPageOfClicks(0, 10, SHORT_LINK, PASSWORD))
                .isNotNull()
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    public void testGetPageOfClicksLinkNotFoundException() {
        when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found", null,
                        "Test Exception".getBytes(), null));

        assertThatThrownBy(() -> countingService.getPageOfClicks(0, 10, SHORT_LINK, PASSWORD))
                .isNotNull()
                .isInstanceOf(LinkNotFoundException.class)
                .hasMessageContaining("Test Exception");
    }

    @Test
    public void testGetPageOfClicksWrongInputException() {
        when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", null,
                        "Test Exception".getBytes(), null));

        assertThatThrownBy(() -> countingService.getPageOfClicks(0, 10, SHORT_LINK, PASSWORD))
                .isNotNull()
                .isInstanceOf(WrongInputException.class)
                .hasMessageContaining("Test Exception");
    }

    @Test
    public void testGetPageOfClicksGettingStatus310() {
        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT));

        assertThatThrownBy(() -> countingService.getPageOfClicks(0, 10, SHORT_LINK, PASSWORD))
                .isNotNull()
                .isInstanceOf(LinkNotFoundException.class)
                .hasMessageContaining("Link was not found");
    }

    @Test
    public void testPostStatistics() {
        ClickDTO clickDTO = new ClickDTO(0, null, null, null, null, null, null);
        countingService.postStatistics(clickDTO);

        assertThatCode(() -> restTemplate
                .postForLocation(anyString(), any())).doesNotThrowAnyException();
    }

    @Test
    public void testUpdateLinkSuccess() {
        when(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        countingService.updateLink(SHORT_LINK, FULL_LINK);

        assertThatCode(() -> restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class))).doesNotThrowAnyException();
    }

    @Test
    public void testUpdateLinkWrongInputException() {
        when(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found", null,
                        "Short link not found".getBytes(), null));

        assertThatThrownBy(() -> countingService.updateLink(SHORT_LINK, FULL_LINK))
                .isNotNull()
                .isInstanceOf(WrongInputException.class)
                .hasMessageContaining("Short link not found");
    }

    @Test
    public void testUpdateLinkLinkNotSecureException() {
        when(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.GONE));

        assertThatThrownBy(() -> countingService.updateLink(SHORT_LINK, FULL_LINK))
                .isNotNull()
                .isInstanceOf(LinkNotSecureException.class);
    }

    @Test
    public void testSetPasswordWrongInputException() {
        when(restTemplate.exchange(anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found", null,
                        "Short link not found".getBytes(), null));

        assertThatThrownBy(() -> countingService.setPassword(SHORT_LINK, FULL_LINK))
                .isNotNull()
                .isInstanceOf(WrongInputException.class)
                .hasMessageContaining("Short link not found");
    }
}



