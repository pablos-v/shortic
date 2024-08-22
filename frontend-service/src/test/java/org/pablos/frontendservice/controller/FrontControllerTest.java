package org.pablos.frontendservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pablos.frontendservice.config.FrontendConfiguration;
import org.pablos.frontendservice.controller.FrontController;
import org.pablos.frontendservice.service.ICountingService;
import org.pablos.frontendservice.service.IGivingService;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.dto.PageDTO;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FrontControllerTest {

    public static final String SHORT_LINK = "shortL";
    public static final String PASSWORD = "12345";
    public static final String FULL_LINK = "fullLink";
    public static final String URL = "http://localhost/";
    @Mock
    private IGivingService iGivingService;

    @Mock
    private ICountingService iCountingService;

    @Mock
    private FrontendConfiguration configuration;
//    @Mock
//    private HttpServletRequest request;
//
//    private HttpSession session;
    private MockHttpSession session;
    @InjectMocks
    private FrontController controller;

    private MockMvc mockMvc;
    
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        session = new MockHttpSession();
    }

    @Test
    public void testMainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("input", new FastLinkDTO()));
    }

    @Test
    public void testGetLink() throws Exception {
        when(iGivingService.clickProcessing(anyString(), any())).thenReturn(FULL_LINK);

        mockMvc.perform(get("/" + SHORT_LINK))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(FULL_LINK));
    }

    @Test
    public void testCreateLink() throws Exception {
        LinkUnitDTO linkUnit = new LinkUnitDTO();
        linkUnit.setShortLink(SHORT_LINK);
        when(configuration.getServerUrl()).thenReturn(URL);
        when(iCountingService.createLink(any())).thenReturn(linkUnit);
        String expectedLink = URL + linkUnit.getShortLink();

        mockMvc.perform(post("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("created"))
                .andExpect(model().attribute("linkUnit", linkUnit))
                .andExpect(model().attribute("shortLink", expectedLink));
    }

    @Test
    public void testUpdateLink() throws Exception {
        doNothing().when(iCountingService).updateLink(anyString(), anyString());
        when(configuration.getServerUrl()).thenReturn(URL);

        mockMvc.perform(put("/")
                .param("shortLink", SHORT_LINK)
                .param("fullLink", FULL_LINK)
                .param("password", PASSWORD)
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/statistics"));

        assertThat(session.getAttribute("serverUrl")).isEqualTo(URL);
        assertThat(session.getAttribute("shortLink")).isEqualTo(SHORT_LINK);
        assertThat(session.getAttribute("password")).isEqualTo(PASSWORD);
    }

    @Test
    public void testSetPassword() throws Exception {
        doNothing().when(iCountingService).setPassword(anyString(), anyString());
        when(configuration.getServerUrl()).thenReturn(URL);

        mockMvc.perform(put("/password")
                .param("shortLink", SHORT_LINK)
                .param("password", PASSWORD)
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/statistics"));

        assertThat(session.getAttribute("serverUrl")).isEqualTo(URL);
        assertThat(session.getAttribute("shortLink")).isEqualTo(SHORT_LINK);
        assertThat(session.getAttribute("password")).isEqualTo(PASSWORD);
    }

    @Test
    public void testGetStatistics() throws Exception {
        when(configuration.getServerUrl()).thenReturn(URL);

        mockMvc.perform(get("/stats")
                .param("shortLink", SHORT_LINK)
                .param("password", PASSWORD)
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/statistics"));

        assertThat(session.getAttribute("serverUrl")).isEqualTo(URL);
        assertThat(session.getAttribute("shortLink")).isEqualTo(SHORT_LINK);
        assertThat(session.getAttribute("password")).isEqualTo(PASSWORD);
    }

    @Test
    public void testShowStatistics() throws Exception {
        session.setAttribute("serverUrl", URL);
        session.setAttribute("shortLink", SHORT_LINK);
        session.setAttribute("password", PASSWORD);
        session.setAttribute("page", 0);
        session.setAttribute("size", 10);
        LinkUnitDTO linkUnit = new LinkUnitDTO();
        when(iCountingService.getPageOfClicks(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(new PageDTO(new ArrayList<>(), 0, linkUnit));

        mockMvc.perform(get("/statistics")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("stat"))
                .andExpect(model().attribute("clicks", new ArrayList<>()))
                .andExpect(model().attribute("linkUnit", linkUnit));

        assertThat(linkUnit.getShortLink()).isEqualTo(URL + SHORT_LINK);
    }

    @Test
    public void testShowStatisticsRedirectsToMainPage() throws Exception {
        mockMvc.perform(get("/statistics")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

}

