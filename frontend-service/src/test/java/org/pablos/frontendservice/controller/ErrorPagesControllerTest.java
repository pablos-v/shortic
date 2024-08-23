package org.pablos.frontendservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class ErrorPagesControllerTest {

    @InjectMocks
    private ErrorPagesController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testNotFound() throws Exception {
        mockMvc.perform(get("/error/404"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"));
    }

    @Test
    public void testWrongInput() throws Exception {
        mockMvc.perform(get("/error/400"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/400"));
    }

    @Test
    public void testServerError() throws Exception {
        mockMvc.perform(get("/error/500"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500"));
    }

    @Test
    public void testWrongPassword() throws Exception {
        mockMvc.perform(get("/error/password"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/password"));
    }
}