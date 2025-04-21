package org.pablos.frontendservice.controller;

import lombok.RequiredArgsConstructor;
import org.pablos.frontendservice.config.FrontendConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки запросов на страницы с юридической информацией.
 */
@Controller
@RequiredArgsConstructor
public class LegacyPagesController {

    private final FrontendConfiguration configuration;

    @GetMapping("/oferta")
    public String showOffer(final Model model){
        model.addAttribute("fromWho", configuration.getFromWho());
        model.addAttribute("thisPageUrl",
                configuration.getServerUrl() + "oferta");
        return "legal/oferta";
    }

    @GetMapping("/privacy")
    public String showPrivacyPolicy(final Model model){
        model.addAttribute("fromWho", configuration.getFromWho());
        model.addAttribute("thisSiteUrl",
                configuration.getServerUrl());
        model.addAttribute("thisPageUrl",
                configuration.getServerUrl() + "privacy");
        return "legal/privacy";
    }

}
