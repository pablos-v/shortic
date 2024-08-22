package org.pablos.frontendservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.pablos.frontendservice.config.FrontendConfiguration;
import org.pablos.frontendservice.service.ICountingService;
import org.pablos.frontendservice.service.IGivingService;
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
    private final IGivingService IGivingService;
    private final ICountingService ICountingService;
    private final FrontendConfiguration configuration;

    @GetMapping
    public String mainPage(final Model model, final @ModelAttribute FastLinkDTO input){
        model.addAttribute("input", input);
        return "index";
    }

    @GetMapping("{shortLink}")
    public RedirectView getLink(final @PathVariable String shortLink, final HttpServletRequest request) {
        String fullLink = IGivingService.clickProcessing(shortLink, request);
        return new RedirectView(fullLink);
    }

    @PostMapping
    public String createLink(
            final FastLinkDTO input,
            final Model model
    ){
        LinkUnitDTO linkUnit = ICountingService.createLink(input);

        model.addAttribute("linkUnit", linkUnit);
        model.addAttribute("shortLink", configuration.getServerUrl() + linkUnit.getShortLink());
        return "created";
    }

    @PutMapping
    public String updateLink(
            @RequestParam String shortLink,
            final @RequestParam String fullLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = PAGE_NUMBER_BY_DEFAULT) int page,
            final @RequestParam(defaultValue = PAGE_SIZE_BY_DEFAULT) int size,
            final HttpSession session
    ){
        shortLink = CommonUtil.clearShortLink(shortLink);
        ICountingService.updateLink(shortLink, fullLink);
        return getStatistics(shortLink, password, page, size, session);
    }

    @PutMapping("/password")
    public String setPassword(
            final @RequestParam String shortLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = PAGE_NUMBER_BY_DEFAULT) int page,
            final @RequestParam(defaultValue = PAGE_SIZE_BY_DEFAULT) int size,
            final HttpSession session
    ){
        ICountingService.setPassword(shortLink, password);
        return getStatistics(shortLink, password, page, size, session);
    }

    /**
     * Переадресует на страницу с результатами статистики.
     * Метод нужен для сокрытия параметров запроса в адресной строке.
     * Предварительно обрезает переданную короткую ссылку, убирая домен, и валидирует.
     * @param shortLink
     * @param password
     * @param page
     * @param size
     * @return редирект на страницу с результатами статистики
     */
    @GetMapping("/stats")
    public String getStatistics(
            @RequestParam String shortLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = PAGE_NUMBER_BY_DEFAULT) int page,
            final @RequestParam(defaultValue = PAGE_SIZE_BY_DEFAULT) int size,
            final HttpSession session
    ){
        shortLink = CommonUtil.clearShortLink(shortLink);
        CommonUtil.validateShortLink(shortLink);

        session.setAttribute("serverUrl", configuration.getServerUrl());
        session.setAttribute("shortLink", shortLink);
        session.setAttribute("password", password);
        session.setAttribute("page", page);
        session.setAttribute("size", size);
        return "redirect:/statistics";
    }

    /**
     * Вызывает страницу с результатами статистики кликов по ссылке.
     * Метод нужен для сокрытия параметров запроса в адресной строке.
     * @param model модель с параметрами запроса
     * @return шаблон страницы с результатами статистики
     */
    @GetMapping("/statistics")
    public String showStatistics(final Model model, final HttpSession session) {

        String serverUrl = (String) session.getAttribute("serverUrl");
        String shortLink = (String) session.getAttribute("shortLink");
        String password = (String) session.getAttribute("password");
        Integer page = (Integer) session.getAttribute("page");
        Integer size = (Integer) session.getAttribute("size");
        if (shortLink == null || password == null || page == null || size == null || serverUrl == null) {
            return "redirect:/";
        }
        PageDTO pageOfClicks = ICountingService.getPageOfClicks(page, size, shortLink, password);
        pageOfClicks.getLinkUnit().setShortLink(serverUrl + shortLink);

        model.addAttribute("clicks", pageOfClicks.getClicks());
        model.addAttribute("clicksPerPage", CLICKS_PER_PAGE);
        model.addAttribute("totalPages", pageOfClicks.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("linkUnit", pageOfClicks.getLinkUnit());
        return "statistics";
    }

}
