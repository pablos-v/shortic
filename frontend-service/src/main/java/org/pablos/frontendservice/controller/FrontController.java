package org.pablos.frontendservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.pablos.frontendservice.service.CountingService;
import org.pablos.frontendservice.service.GivingService;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.dto.PageDTO;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FrontController {

    public static final List<Integer> CLICKS_PER_PAGE = Arrays.asList(10, 25, 50);
    public static final String PAGE_SIZE_BY_DEFAULT = "10";
    public static final String PAGE_NUMBER_BY_DEFAULT = "1";
    private final GivingService givingService;
    private final CountingService countingService;

    @GetMapping
    public String mainPage(final Model model, final @ModelAttribute FastLinkDTO input){
        model.addAttribute("input", input);
        model.addAttribute("isMainPage", true);

        return "index";
    }

    @GetMapping("{shortLink}")
    public RedirectView getLink(final @PathVariable String shortLink, final HttpServletRequest request) {
        String fullLink = givingService.clickProcessing(shortLink, request);
        return new RedirectView(fullLink);
    }

    @PostMapping
    public String createLink(
            final FastLinkDTO input,
            final Model model,
            HttpServletRequest request
    ){
        LinkUnitDTO linkUnit = countingService.createLink(input);
        String serverUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

        model.addAttribute("linkUnit", linkUnit);
        model.addAttribute("shortLink", serverUrl + linkUnit.getShortLink());
        model.addAttribute("isMainPage", false);

        return "created";
    }

    @PutMapping
    public String updateLink(
            final @RequestParam String shortLink,
            final @RequestParam String fullLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = PAGE_NUMBER_BY_DEFAULT) int page,
            final @RequestParam(defaultValue = PAGE_SIZE_BY_DEFAULT) int size,
            final Model model
    ){
        countingService.updateLink(shortLink, fullLink);

        return showStatistics(shortLink, password, page, size, model);
    }

    @PutMapping("/password")
    public String setPassword(
            final @RequestParam String shortLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = PAGE_NUMBER_BY_DEFAULT) int page,
            final @RequestParam(defaultValue = PAGE_SIZE_BY_DEFAULT) int size,
            final Model model
    ){
        countingService.setPassword(shortLink, password);

        return showStatistics(shortLink, password, page, size, model);
    }

    /**
     * Вызывает страницу с результатами статистики кликов по ссылке.
     * Предварительно обрезает переданную короткую ссылку, убирая домен, и валидирует.
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/stats")
    public String showStatistics(
            @RequestParam String shortLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = PAGE_NUMBER_BY_DEFAULT) int page,
            final @RequestParam(defaultValue = PAGE_SIZE_BY_DEFAULT) int size,
            final Model model
    ){
        shortLink = shortLink.trim();
        int length = shortLink.length();
        shortLink = shortLink.substring(length-6, length);

        CommonUtil.validateShortLink(shortLink);
        PageDTO pageOfClicks = countingService.getPageOfClicks(page, size, shortLink, password);

        model.addAttribute("clicks", pageOfClicks.getClicks());
        model.addAttribute("clicksPerPage", CLICKS_PER_PAGE);
        model.addAttribute("totalPages", pageOfClicks.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        
        model.addAttribute("linkUnit", pageOfClicks.getLinkUnit());
        model.addAttribute("isMainPage", false);

        return "stats";
//        return "redirect:/stats";
    }


    @GetMapping("/error/404")
    public String notFound(final Model model){
        model.addAttribute("isMainPage", false);
        return "error/404";
    }
    @GetMapping("/error/400")
    public String wrongInput(final Model model){
        model.addAttribute("isMainPage", false);
        return "error/400";
    }
    @GetMapping("/error/410")
    public String badLink(final Model model){
        model.addAttribute("isMainPage", false);
        return "error/410";
    }
    @GetMapping("/error/password")
    public String wrongPassword(final Model model){
        model.addAttribute("isMainPage", false);
        return "error/password";
    }
    @GetMapping("/oferta")
    public String showOffer(final Model model){
        model.addAttribute("isMainPage", false);
        return "oferta";
    }

}
