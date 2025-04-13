package org.pablos.backendcountingservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.UpdatingFastLinkException;
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
        LinkNotFoundException e = new LinkNotFoundException("Link not found exception");

        String result = exceptionHandlingService.onLinkNotFoundException(e);

        verify(logger, times(1)).warn(e.getMessage(), e);
        assertThat(result).isEqualTo(e.getMessage());
    }

    @Test
    public void testOnWrongPasswordException() {
        WrongPasswordException e = new WrongPasswordException();

        String result = exceptionHandlingService.onWrongPasswordException(e);

        verify(logger, times(1)).warn(e.getMessage(), e);
        assertThat(result).isEqualTo(e.getMessage());
    }

    @Test
    public void testOnLinkNotFoundWhileActivationException() {
        LinkNotFoundWhileActivationException e = new LinkNotFoundWhileActivationException();

        exceptionHandlingService.onLinkNotFoundWhileActivationException(e);

        verify(logger, times(1)).error(e.getMessage(), e);
    }

    @Test
    public void testOnAnyChangingFastLinkException() {
        Exception e = new SavingFastLinkException("Saving fast link exception");

        exceptionHandlingService.onAnyChangingFastLinkException(e);

        verify(logger, times(1)).error(e.getMessage(), e);
    }

    @Test
    public void testOnAnyChangingFastLinkExceptionWithUpdatingFastLinkException() {
        Exception e = new UpdatingFastLinkException("Updating fast link exception");

        exceptionHandlingService.onAnyChangingFastLinkException(e);

        verify(logger, times(1)).error(e.getMessage(), e);
    }

    @Test
    public void testOnAnyChangingFastLinkExceptionWithDeletingFastLinkException() {
        Exception e = new DeletingFastLinkException("Deleting fast link exception");

        exceptionHandlingService.onAnyChangingFastLinkException(e);

        verify(logger, times(1)).error(e.getMessage(), e);
    }

}

