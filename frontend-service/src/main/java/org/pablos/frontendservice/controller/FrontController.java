package org.pablos.frontendservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.pablos.frontendservice.service.CountingService;
import org.pablos.frontendservice.service.GivingService;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.PageDTO;
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
            final Model model,
            final @ModelAttribute FastLinkDTO input,
            final @ModelAttribute LinkUnitDTO dtoForStats){
        // TODO вместо ModelAttribute просто создавать новые объекты и сетить в модель?
        model.addAttribute("input", input);
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", true);

        return "index";
    }

    @GetMapping("{link}")
    public RedirectView getLink(final @PathVariable String link, final HttpServletRequest request) {
        String fullLink = givingService.clickProcessing(link, request);

        return new RedirectView("/" + Objects.requireNonNullElse(fullLink, "error/404"));
    }

    @PostMapping
    public String createLink(
            final @RequestBody FastLinkDTO input,
            final @ModelAttribute LinkUnitDTO dtoForStats,
            final Model model
    ){
        CommonUtil.validateDTOFullLink(input);
        LinkUnitDTO linkUnit = countingService.createLink(input);
        model.addAttribute("linkUnit", linkUnit);
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", false);

        return "created";
    }

    @PutMapping
    public String updateLink(
            @RequestBody LinkUnitDTO linkUnit,
            final @ModelAttribute LinkUnitDTO dtoForStats,
            final Model model,
            final @RequestParam(defaultValue = "1") int page,
            final @RequestParam(defaultValue = "10") int size
    ){
        PageDTO pageOfClicks = countingService.updateAndGetPageOfClicks(page, size, linkUnit);

        return preparePageableStats(dtoForStats, model, page, size, pageOfClicks);
    }

    @GetMapping("/stats")
    public String showStatistics(
            @RequestBody LinkUnitDTO linkUnit,
            final @ModelAttribute LinkUnitDTO dtoForStats,
            final Model model,
            final @RequestParam(defaultValue = "1") int page,
            final @RequestParam(defaultValue = "10") int size
    ){
        CommonUtil.validateDTOShortLink(linkUnit);
        PageDTO pageOfClicks = countingService.getPageOfClicks(page, size, linkUnit);

        return preparePageableStats(dtoForStats, model, page, size, pageOfClicks);
    }

    private String preparePageableStats(LinkUnitDTO dtoForStats, Model model,
            int page, int size, PageDTO pageOfClicks) {
        model.addAttribute("clicks", pageOfClicks.getClicks());
//        model.addAttribute("totalItems", pageClicks.getTotalElements());
        model.addAttribute("totalPages", pageOfClicks.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("linkUnit", pageOfClicks.getLinkUnit());
        model.addAttribute("isMainPage", false);

        return "stats";
    }
    @GetMapping("/error/404")
    public String notFound(final @ModelAttribute LinkUnitDTO dtoForStats, final Model model){
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", false);
        return "error/404";
    }
    @GetMapping("/error/400")
    public String wrongInput(final @ModelAttribute LinkUnitDTO dtoForStats, final Model model){
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", false);
        return "error/400";
    }
    @GetMapping("/error/410")
    public String badLink(final @ModelAttribute LinkUnitDTO dtoForStats, final Model model){
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", false);
        return "error/410";
    }
    @GetMapping("/error/password")
    public String wrongPassword(final @ModelAttribute LinkUnitDTO dtoForStats, final Model model){
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", false);
        return "error/password";
    }
    @GetMapping("/oferta")
    public String showOffer(final @ModelAttribute LinkUnitDTO dtoForStats, final Model model){
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", false);
        return "oferta";
    }


}
