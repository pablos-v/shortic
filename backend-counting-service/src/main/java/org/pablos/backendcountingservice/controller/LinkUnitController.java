package org.pablos.backendcountingservice.controller;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.service.ILinkUnitService;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.dto.LinkUnitDTO;
import org.pablos.common.dto.PageDTO;
import org.pablos.common.util.CommonUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
public class LinkUnitController {

    private final ILinkUnitService linkUnitService;

    @PostMapping
    public ResponseEntity<LinkUnitDTO> createLinkUnit(final @RequestBody FastLinkDTO input) {

        LinkUnitDTO link = linkUnitService.createLinkUnit(input);

        return ResponseEntity.ok(link);
    }

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

    @PutMapping
    public ResponseEntity<Void> updateFullLinkInLinkUnit(
            final @RequestParam String shortLink,
            final @RequestParam String fullLink
    ) {
        linkUnitService.updateLinkUnit(shortLink, fullLink);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> setPassword(
            final @RequestParam String shortLink,
            final @RequestParam String password
    ) {
        linkUnitService.setPassword(shortLink, password);

        return ResponseEntity.ok().build();
    }

}
