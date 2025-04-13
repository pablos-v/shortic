package org.pablos.backendcountingservice.controller;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.service.ILinkUnitService;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.dto.LinkUnitDTO;
import org.pablos.common.dto.PageDTO;
import org.pablos.common.util.CommonUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы со ссылками.
 */
@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
public class LinkUnitController {

    private final ILinkUnitService linkUnitService;

    /**
     * Создает новую ссылку.
     *
     * @param input DTO с информацией о новой ссылке.
     * @return DTO с информацией о созданной ссылке.
     */
    @PostMapping
    public ResponseEntity<LinkUnitDTO> createLinkUnit(final @RequestBody FastLinkDTO input) {

        LinkUnitDTO link = linkUnitService.createLinkUnit(input);

        return ResponseEntity.ok(link);
    }

    /**
     * Возвращает страницу ссылок.
     *
     * @param page номер страницы.
     * @param size размер страницы.
     * @param shortLink короткая ссылка.
     * @param password пароль.
     * @return DTO с информацией о странице ссылок.
     */
    @GetMapping
    public ResponseEntity<PageDTO> getPage(
            int page,
            int size,
            String shortLink,
            String password
    ) {
        CommonUtil.validateShortLink(shortLink);

        PageDTO pageDTO = linkUnitService.getPage(page, size, shortLink, password);

        return ResponseEntity.ok(pageDTO);
    }

    /**
     * Обновляет полную ссылку в ссылке.
     *
     * @param shortLink короткая ссылка.
     * @param fullLink полная ссылка.
     * @return ответ с кодом 200 OK.
     */
    @PutMapping
    public ResponseEntity<Void> updateFullLinkInLinkUnit(
            final @RequestParam String shortLink,
            final @RequestParam String fullLink
    ) {
        linkUnitService.updateLinkUnit(shortLink, fullLink);

        return ResponseEntity.ok().build();
    }

    /**
     * Устанавливает пароль для ссылки.
     *
     * @param shortLink короткая ссылка.
     * @param password пароль.
     * @return ответ с кодом 200 OK.
     */
    @PutMapping("/password")
    public ResponseEntity<Void> setPassword(
            final @RequestParam String shortLink,
            final @RequestParam String password
    ) {
        linkUnitService.setPassword(shortLink, password);

        return ResponseEntity.ok().build();
    }

}
