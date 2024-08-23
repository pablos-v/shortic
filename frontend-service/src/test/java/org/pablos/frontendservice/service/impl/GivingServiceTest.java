package org.pablos.frontendservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.frontendservice.service.ICountingService;
import org.pablos.common.dto.ClickDTO;
import org.pablos.common.exception.LinkNotFoundException;
import org.pablos.common.exception.WrongInputException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GivingServiceTest {

    private static final String SHORT_LINK = "shortL";
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ICountingService countingService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GivingService givingService;

    @Test
    public void testClickProcessingSuccess() throws InterruptedException {
        String expectedResponse = "Success";

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(expectedResponse);

        String response = givingService.clickProcessing(SHORT_LINK, request);

        assertThat(response).isEqualTo(expectedResponse);
// без задержки времени выполнения, тестируемый метод не успевает увидеть запуска метода postStatistics и тест фейлится
        Thread.sleep(1000);
        verify(countingService, times(1)).postStatistics(any(ClickDTO.class));
    }

    @Test
    public void testClickProcessingBadRequest() throws InterruptedException {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", null
                , "Test Exception".getBytes(), null);

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(exception);

        assertThatThrownBy(() -> givingService.clickProcessing(SHORT_LINK, request))
                .isNotNull()
                .isInstanceOf(WrongInputException.class)
                .hasMessageContaining("Test Exception");
        Thread.sleep(1000);
        verify(countingService, times(1)).postStatistics(any(ClickDTO.class));
    }

    @Test
    public void testClickProcessingNotFound() throws InterruptedException {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found", null,
                "Test Exception".getBytes(), null);

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(exception);

        assertThatThrownBy(() -> givingService.clickProcessing(SHORT_LINK, request))
                .isNotNull()
                .isInstanceOf(LinkNotFoundException.class)
                .hasMessageContaining("Test Exception");
        Thread.sleep(1000);
        verify(countingService, times(1)).postStatistics(any(ClickDTO.class));
    }

    @Test
    public void testClickProcessingUnknownError() throws InterruptedException {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(exception);

        assertThatThrownBy(() -> givingService.clickProcessing(SHORT_LINK, request))
                .isNotNull()
                .isInstanceOf(WrongInputException.class)
                .hasMessage("Unknown error");
        Thread.sleep(1000);
        verify(countingService, times(1)).postStatistics(any(ClickDTO.class));
    }

}