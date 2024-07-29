package org.pablos.frontendservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.pablos.frontendservice.service.CountingService;
import org.pablos.frontendservice.service.GivingService;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FrontController {

    private final GivingService givingService;
    private final CountingService countingService;

    @GetMapping
    public String mainPage(final @ModelAttribute FastLinkDTO input, final Model model){
//        model.addAttribute("input", new FastLinkDTO()); TODO можно попробовать вместо @ModelAttribute
        model.addAttribute("input", input);

        return "index";
    }

    @GetMapping("{link}")
    public RedirectView getLink(final @PathVariable String link, final HttpServletRequest request) {
        String fullLink = givingService.clickProcessing(link, request);

        return new RedirectView("/" + Objects.requireNonNullElse(fullLink, "404"));
    }

    @PostMapping
    public String createLink(final @ModelAttribute FastLinkDTO input, final Model model){
        CommonUtil.validateDTOFullLink(input);
        FastLinkDTO linkDTO = countingService.getLink(input);
        model.addAttribute("shortLink", linkDTO.getShortLink());
        model.addAttribute("fullLink", linkDTO.getFullLink());

        return "created";
    }


}
