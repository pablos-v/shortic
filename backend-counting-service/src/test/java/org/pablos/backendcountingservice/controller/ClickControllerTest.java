package org.pablos.backendcountingservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.backendcountingservice.service.IClickService;
import org.pablos.common.dto.ClickDTO;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClickControllerTest {

    @Mock
    private IClickService clickService;

    @InjectMocks
    private ClickController clickController;

    @Test
    public void testCreateClick() {
        ClickDTO clickDTO = new ClickDTO(-1,null,null,null,null,null, null);
        ResponseEntity<Void> responseEntity = clickController.createClick(clickDTO);

        verify(clickService, times(1)).createClick(clickDTO);
        assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build());
    }

}


