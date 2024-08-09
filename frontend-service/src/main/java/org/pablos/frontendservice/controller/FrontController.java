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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FrontController {

    public static final List<Integer> CLICKS_PER_PAGE = Arrays.asList(10, 25, 50);
    private final GivingService givingService;
    private final CountingService countingService;

    @GetMapping
    public String mainPage(
            final Model model,
            final @ModelAttribute FastLinkDTO input,
            final @ModelAttribute LinkUnitDTO dtoForStats){
        model.addAttribute("input", input);
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", true);

        return "index";
    }

    @GetMapping("{link}")
    public RedirectView getLink(final @PathVariable String link, final HttpServletRequest request) {
        String fullLink = givingService.clickProcessing(link, request);

        return new RedirectView(Objects.requireNonNullElse(fullLink, "/error/404"));
    }

    @PostMapping
    public String createLink(
            final FastLinkDTO input,
            final @ModelAttribute LinkUnitDTO dtoForStats,
            final Model model,
            HttpServletRequest request
    ){
        CommonUtil.validateDTOFullLink(input);
        LinkUnitDTO linkUnit = countingService.createLink(input);
        String serverUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

        model.addAttribute("linkUnit", linkUnit);
        model.addAttribute("shortLink", serverUrl + linkUnit.getShortLink());
        model.addAttribute("dtoForStats", dtoForStats);
        model.addAttribute("isMainPage", false);

        return "created";
    }

    @PutMapping
    public String updateLink(
            LinkUnitDTO linkUnit,
            final @ModelAttribute LinkUnitDTO dtoForStats,
            final Model model,
            final @RequestParam(defaultValue = "1") int page,
            final @RequestParam(defaultValue = "10") int size
    ){
        PageDTO pageOfClicks = countingService.updateAndGetPageOfClicks(page, size, linkUnit);

        return preparePageableStats(dtoForStats, model, page, size, pageOfClicks);
    }

    /**
     * Вызывает страницу с результатами статистики кликов по ссылке.
     * Предварительно обрезает переданную короткую ссылку, убирая домен, и валидирует.
     * @param dtoForStats
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/stats")
    public String showStatistics(
            @RequestParam String shortLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = "1") int page,
            final @RequestParam(defaultValue = "10") int size,
            final @ModelAttribute LinkUnitDTO dtoForStats,
            final Model model
    ){
        int length = shortLink.length();
        shortLink = shortLink.trim().substring(length-6, length);

        CommonUtil.validateShortLink(shortLink);
        PageDTO pageOfClicks = countingService.getPageOfClicks(page, size, shortLink, password);

        return preparePageableStats(dtoForStats, model, page, size, pageOfClicks);
    }

    private String preparePageableStats(LinkUnitDTO dtoForStats, Model model,
            int page, int size, PageDTO pageOfClicks) {
        model.addAttribute("clicks", pageOfClicks.getClicks());
        model.addAttribute("clicksPerPage", CLICKS_PER_PAGE);
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
