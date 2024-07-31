package org.pablos.frontendservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.pablos.frontendservice.service.CountingService;
import org.pablos.frontendservice.service.GivingService;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
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
    public String mainPage(
            final @ModelAttribute FastLinkDTO input,
            final Model model,
            final @ModelAttribute LinkUnitDTO dto){
//        model.addAttribute("input", new FastLinkDTO()); TODO можно попробовать вместо @ModelAttribute
        model.addAttribute("input", input);
        model.addAttribute("dto", dto);

        return "index";
    }

    @GetMapping("{link}")
    public RedirectView getLink(final @PathVariable String link, final HttpServletRequest request) {
        String fullLink = givingService.clickProcessing(link, request);

        return new RedirectView("/" + Objects.requireNonNullElse(fullLink, "404"));
    }

    @PostMapping
    public String createLink(
            final @ModelAttribute FastLinkDTO input,
            final Model model,
            final @ModelAttribute LinkUnitDTO dto){
        CommonUtil.validateDTOFullLink(input);
        FastLinkDTO linkDTO = countingService.createLink(input);
        model.addAttribute("shortLink", linkDTO.getShortLink());
        model.addAttribute("fullLink", linkDTO.getFullLink());
        model.addAttribute("dto", dto);

        return "created";
    }
    @GetMapping("/stats")
    public String showStatistics(final @ModelAttribute LinkUnitDTO dto, final Model model){
        CommonUtil.validateDTOShortLink(dto);

        LinkUnitDTO linkUnit = countingService.getLinkUnit(dto);

        model.addAttribute("dto", dto);
        model.addAttribute("linkUnit", linkUnit);
        return "stats";
    }
    @GetMapping("/404")
    public String notFound(final @ModelAttribute LinkUnitDTO dto, final Model model){
        model.addAttribute("dto", dto);
        return "404";
    }
    @GetMapping("/400")
    public String wrongPassword(final @ModelAttribute LinkUnitDTO dto, final Model model){
        model.addAttribute("dto", dto);
        return "400";
    }
    @GetMapping("/oferta")
    public String showOffer(final @ModelAttribute LinkUnitDTO dto, final Model model){
        model.addAttribute("dto", dto);
        return "oferta";
    }


}
