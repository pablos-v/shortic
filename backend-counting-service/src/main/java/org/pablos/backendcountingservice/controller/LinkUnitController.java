package org.pablos.backendcountingservice.controller;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.service.LinkUnitService;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.dto.PageDTO;
import org.pablos.shortic.dto.PageRequestDTO;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
public class LinkUnitController {

    private final LinkUnitService linkUnitService;

    @PostMapping
    public ResponseEntity<LinkUnitDTO> createLinkUnit(final @RequestBody FastLinkDTO input) {
        CommonUtil.validateDTOFullLink(input);

        LinkUnitDTO link = linkUnitService.createLinkUnit(input);

        return ResponseEntity.ok(link);
    }

    @GetMapping
    public ResponseEntity<PageDTO> getPage(
//            final @RequestBody PageRequestDTO dto
            int page,
            int size,
            String shortLink,
            String password
    ) {
        CommonUtil.validateDTOShortLink(new FastLinkDTO(shortLink, ""));

        PageDTO pageDTO = linkUnitService.getPage(page,size, shortLink, password);

        return ResponseEntity.ok(pageDTO);
    }

    @PutMapping
    public ResponseEntity<PageDTO> updateLinkUnitAndGetPage(final @RequestBody PageRequestDTO dto) {
        CommonUtil.validateDTOFullLink(dto.getLinkUnit());

        PageDTO pageDTO = linkUnitService.updateLinkUnitAndGetPage(dto);

        return ResponseEntity.ok(pageDTO);
    }

}
