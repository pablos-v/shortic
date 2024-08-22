package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.shortic.exception.*;
import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlingServiceTest {

    @Mock
    private Logger logger;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ExceptionHandlingService exceptionHandlingService;

    @Test
    public void testOnLinkProcessingException() throws IOException {
        exceptionHandlingService.onLinkProcessingException(response, new LinkProcessingException("message"));

        verify(logger).warn(anyString(), (Exception) any());
        verify(response).sendRedirect("/error/404");
    }

    @Test
    public void testOnLinkNotFoundException() throws IOException {
        exceptionHandlingService.onLinkProcessingException(response, new LinkNotFoundException());

        verify(logger).warn(anyString(), (Exception) any());
        verify(response).sendRedirect("/error/404");
    }

    @Test
    public void testOnObjectNotProvidedException() throws IOException {
        exceptionHandlingService.onLinkProcessingException(response, new ObjectNotProvidedException());

        verify(logger).warn(anyString(), (Exception) any());
        verify(response).sendRedirect("/error/404");
    }

    @Test
    public void testOnWrongPasswordException() throws IOException {
        exceptionHandlingService.onWrongPasswordException(response, new WrongPasswordException());

        verify(logger).warn(anyString(), (Exception) any());
        verify(response).sendRedirect("/error/password");
    }

    @Test
    public void testOnLinkNotSecureException() throws IOException {
        exceptionHandlingService.onLinkNotSecureException(response, new LinkNotSecureException());

        verify(logger).warn(anyString(), (Exception) any());
        verify(response).sendRedirect("/error/410");
    }

    @Test
    public void testOnWrongInputException() {
        String testException = "Test exception";

        ModelAndView modelAndView = exceptionHandlingService
                .onWrongInputException(new WrongInputException(testException));

        verify(logger).warn(anyString(), (Exception) any());
        assertThat(modelAndView.getViewName()).isEqualTo("/error/400");
        assertThat(modelAndView.getModel().get("message")).isEqualTo(testException);
    }

    @Test
    public void testOnFullLinkNotProvidedException() {
        ModelAndView modelAndView = exceptionHandlingService
                .onWrongInputException(new FullLinkNotProvidedException());

        verify(logger).warn(anyString(), (Exception) any());
        assertThat(modelAndView.getViewName()).isEqualTo("/error/400");
        assertThat(modelAndView.getModel().get("message")).isEqualTo("Full link was not provided");
    }

    @Test
    public void testOnFullLinkSizeException() {
        ModelAndView modelAndView = exceptionHandlingService
                .onWrongInputException(new FullLinkSizeException());

        verify(logger).warn(anyString(), (Exception) any());
        assertThat(modelAndView.getViewName()).isEqualTo("/error/400");
        assertThat(modelAndView.getModel().get("message")).isEqualTo("Full link size exceeds the limit");
    }

    @Test
    public void testOnFullLinkFormatException() {
        ModelAndView modelAndView = exceptionHandlingService
                .onWrongInputException(new FullLinkFormatException());

        verify(logger).warn(anyString(), (Exception) any());
        assertThat(modelAndView.getViewName()).isEqualTo("/error/400");
        assertThat(modelAndView.getModel().get("message"))
                .isEqualTo("Link must start with http:// or https:// and end with .someDomain");
    }
}