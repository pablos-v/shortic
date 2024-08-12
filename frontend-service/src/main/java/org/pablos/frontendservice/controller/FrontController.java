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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
            final RedirectAttributes redirectAttributes
    ){
        countingService.updateLink(shortLink, fullLink);

        return getStatistics(shortLink, password, page, size, redirectAttributes);
    }

    @PutMapping("/password")
    public String setPassword(
            final @RequestParam String shortLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = PAGE_NUMBER_BY_DEFAULT) int page,
            final @RequestParam(defaultValue = PAGE_SIZE_BY_DEFAULT) int size,
            final RedirectAttributes redirectAttributes
    ){
        countingService.setPassword(shortLink, password);

        return getStatistics(shortLink, password, page, size, redirectAttributes);
    }

    /**
     * Переадресует на страницу с результатами статистики.
     * Метод нужен для сокрытия параметров запроса в адресной строке.
     * Предварительно обрезает переданную короткую ссылку, убирая домен, и валидирует.
     * @param shortLink
     * @param password
     * @param page
     * @param size
     * @param redirectAttributes - RedirectAttributes для скрытой передачи параметров запроса при редиректе
     * @return редирект на страницу с результатами статистики
     */
    @GetMapping("/stats")
    public String getStatistics(
            @RequestParam String shortLink,
            final @RequestParam String password,
            final @RequestParam(defaultValue = PAGE_NUMBER_BY_DEFAULT) int page,
            final @RequestParam(defaultValue = PAGE_SIZE_BY_DEFAULT) int size,
            final RedirectAttributes redirectAttributes
    ){
        shortLink = CommonUtil.clearShortLink(shortLink);

        CommonUtil.validateShortLink(shortLink);

        // Сохраняем параметры в RedirectAttributes
        redirectAttributes.addFlashAttribute("shortLink", shortLink);
        redirectAttributes.addFlashAttribute("password", password);
        redirectAttributes.addFlashAttribute("page", page);
        redirectAttributes.addFlashAttribute("size", size);

        return "redirect:/statistics";
    }

    /**
     * Вызывает страницу с результатами статистики кликов по ссылке.
     * Метод нужен для сокрытия параметров запроса в адресной строке.
     * @param model модель с параметрами запроса
     * @return шаблон страницы с результатами статистики
     */
    @GetMapping("/statistics")
    public String showStatistics(final Model model, final HttpServletRequest request){

        String shortLink = (String) model.asMap().get("shortLink");
        String password = (String) model.asMap().get("password");
        int page = (int) model.asMap().get("page");
        int size = (int) model.asMap().get("size");

        PageDTO pageOfClicks = countingService.getPageOfClicks(page, size, shortLink, password);

        String serverUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        pageOfClicks.getLinkUnit().setShortLink(serverUrl + shortLink);

        model.addAttribute("clicks", pageOfClicks.getClicks());
        model.addAttribute("clicksPerPage", CLICKS_PER_PAGE);
        model.addAttribute("totalPages", pageOfClicks.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("linkUnit", pageOfClicks.getLinkUnit());
        model.addAttribute("isMainPage", false);

        return "statistics";
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
