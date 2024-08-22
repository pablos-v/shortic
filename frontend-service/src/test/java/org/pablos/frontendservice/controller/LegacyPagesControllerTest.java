package org.pablos.frontendservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.frontendservice.config.FrontendConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(MockitoExtension.class)
class LegacyPagesControllerTest {
    @Mock
    private FrontendConfiguration configuration;
    @InjectMocks
    private LegacyPagesController legacyPagesController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(legacyPagesController).build();
    }
    @Test
    public void testShowOferta() throws Exception {
        when(configuration.getFromWho()).thenReturn("Name Surname");
        when(configuration.getServerUrl()).thenReturn("http://localhost/");

        mockMvc.perform(get("/oferta"))
                .andExpect(view().name("legal/oferta"))
                .andExpect(model().attribute("fromWho", "Name Surname"))
                .andExpect(model().attribute("thisPageUrl", "http://localhost/oferta"));
    }
    @Test
    public void testShowPrivacyPolicy() throws Exception {
        when(configuration.getFromWho()).thenReturn("Name Surname");
        when(configuration.getServerUrl()).thenReturn("http://localhost/");

        mockMvc.perform(get("/privacy"))
                .andExpect(view().name("legal/privacy"))
                .andExpect(model().attribute("fromWho", "Name Surname"))
                .andExpect(model().attribute("thisSiteUrl", "http://localhost/"))
                .andExpect(model().attribute("thisPageUrl", "http://localhost/privacy"));
    }

}