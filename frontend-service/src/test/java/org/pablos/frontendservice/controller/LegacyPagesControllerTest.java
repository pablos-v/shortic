package org.pablos.frontendservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.frontendservice.config.FrontendConfiguration;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LegacyPagesControllerTest {
    @Mock
    private FrontendConfiguration configuration;
    @Mock
    private Model model;
    Map<String, String> attributes;

    @InjectMocks
    private LegacyPagesController legacyPagesController;

    @BeforeEach
    public void setUp() {
        attributes = new HashMap<>();
    }

    @Test
    public void testShowOffer() {
        when(configuration.getFromWho()).thenReturn("Name Surname");
        when(configuration.getServerUrl()).thenReturn("http://localhost/");
        doAnswer(invocation -> {
            String attributeName = invocation.getArgument(0);
            String attributeValue = invocation.getArgument(1);
            attributes.put(attributeName, attributeValue);
            return null;
        }).when(model).addAttribute(anyString(), any());

        String viewName = legacyPagesController.showOffer(model);

        assertThat(viewName).isEqualTo("legal/oferta");
        assertThat(attributes.get("fromWho")).isEqualTo("Name Surname");
        assertThat(attributes.get("thisPageUrl")).isEqualTo("http://localhost/oferta");
    }

    @Test
    public void testShowPrivacyPolicy() {
        when(configuration.getFromWho()).thenReturn("Name Surname");
        when(configuration.getServerUrl()).thenReturn("http://localhost/");
        doAnswer(invocation -> {
            String attributeName = invocation.getArgument(0);
            String attributeValue = invocation.getArgument(1);
            attributes.put(attributeName, attributeValue);
            return null;
        }).when(model).addAttribute(anyString(), any());

        String viewName = legacyPagesController.showPrivacyPolicy(model);

        assertThat(viewName).isEqualTo("legal/privacy");
        assertThat(attributes.get("fromWho")).isEqualTo("Name Surname");
        assertThat(attributes.get("thisSiteUrl")).isEqualTo("http://localhost/");
        assertThat(attributes.get("thisPageUrl")).isEqualTo("http://localhost/privacy");
    }

}