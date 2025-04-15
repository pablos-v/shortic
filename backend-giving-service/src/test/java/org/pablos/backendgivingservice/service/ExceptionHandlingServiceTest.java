package org.pablos.backendgivingservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.backendgivingservice.service.ExceptionHandlingService;
import org.pablos.common.exception.*;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlingServiceTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private ExceptionHandlingService exceptionHandlingService;

    @Test
    public void testOnLinkProcessingException() {
        Exception e = new LinkProcessingException("Link processing exception");

        String result = exceptionHandlingService.onLinkProcessingException(e);

        verify(logger, times(1)).warn(e.getMessage(), e);
        assertThat(result).isEqualTo(e.getMessage());
    }

    @Test
    public void testOnLinkNotFoundException() {
        Exception e = new LinkNotFoundException("Link not found exception");

        String result = exceptionHandlingService.onLinkNotFoundException(e);

        verify(logger, times(1)).warn(e.getMessage(), e);
        assertThat(result).isEqualTo(e.getMessage());
    }

    @Test
    public void testOnLinkProcessingExceptionWithFullLinkSizeException() {
        Exception e = new FullLinkSizeException();

        String result = exceptionHandlingService.onLinkProcessingException(e);

        verify(logger, times(1)).warn(e.getMessage(), e);
        assertThat(result).isEqualTo(e.getMessage());
    }

    @Test
    public void testOnLinkProcessingExceptionWithFullLinkFormatException() {
        Exception e = new FullLinkFormatException();

        String result = exceptionHandlingService.onLinkProcessingException(e);

        verify(logger, times(1)).warn(e.getMessage(), e);
        assertThat(result).isEqualTo(e.getMessage());
    }

    @Test
    public void testOnLinkProcessingExceptionWithObjectNotProvidedException() {
        Exception e = new ObjectNotProvidedException();

        String result = exceptionHandlingService.onLinkProcessingException(e);

        verify(logger, times(1)).warn(e.getMessage(), e);
        assertThat(result).isEqualTo(e.getMessage());
    }

    @Test
    public void testOnLinkProcessingExceptionWithFullLinkNotProvidedException() {
        Exception e = new FullLinkNotProvidedException();

        String result = exceptionHandlingService.onLinkProcessingException(e);

        verify(logger, times(1)).warn(e.getMessage(), e);
        assertThat(result).isEqualTo(e.getMessage());
    }

}



