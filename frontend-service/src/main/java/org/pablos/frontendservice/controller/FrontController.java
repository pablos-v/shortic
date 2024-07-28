package org.pablos.frontendservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.pablos.frontendservice.service.CountingService;
import org.pablos.frontendservice.service.GivingService;
import org.pablos.shortic.dto.FastLinkDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class FrontController {

    private final GivingService givingService;
    private final CountingService countingService;

    @GetMapping
    public String mainPage(Model model){
        model.addAttribute("input", new FastLinkDTO());
        return "index";
    }

    @GetMapping("{link}")
    public RedirectView getLink(@PathVariable String link, HttpServletRequest request) {

        String fullLink = givingService.clickProcessing(link, request);

        // Обработка ситуации, когда сокращенная ссылка не найдена
        return new RedirectView("/" + Objects.requireNonNullElse(fullLink, "404"));
    }

    @PostMapping
    public String createLink(@ModelAttribute FastLinkDTO input, Model model){
        FastLinkDTO linkDTO = countingService.getLink(input);
        model.addAttribute("shortLink", linkDTO.getShortLink());
        model.addAttribute("fullLink", linkDTO.getFullLink());
        return "created";
    }


}
