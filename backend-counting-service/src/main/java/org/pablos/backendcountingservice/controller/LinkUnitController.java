package org.pablos.backendcountingservice.controller;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.service.LinkUnitService;
import org.pablos.shortic.dto.FastLinkDTO;
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

        // вернёт короткую ссылку
        FastLinkDTO link = linkUnitService.createLinkUnit(input);

        // new Thread check in YA completed future
        // post to giving if ok

        return ResponseEntity.ok(link);
    }

}
