package org.pablos.backendcountingservice.controller;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.service.LinkUnitService;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
public class LinkUnitController {

    private final LinkUnitService linkUnitService;

    @PostMapping
    public ResponseEntity<FastLinkDTO> createLinkUnit(final @RequestBody FastLinkDTO input) {
        CommonUtil.validateDTOFullLink(input);

        FastLinkDTO link = linkUnitService.createLinkUnit(input);

        return ResponseEntity.ok(link);
    }

    @PostMapping
    public ResponseEntity<LinkUnitDTO> getLinkUnit(final @RequestBody LinkUnitDTO dto) {
        CommonUtil.validateDTOShortLink(dto);

        LinkUnitDTO link = linkUnitService.getLinkUnit(dto);

        return ResponseEntity.ok(link);
    }

}
