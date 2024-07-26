package org.pablos.backendcountingservice.controller;

import jakarta.validation.Valid;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LinkUnitController {

    /**
     *                 "link", link,
     *                 "ipAddress", ipAddress,
     *                 "userAgent", userAgent,
     *                 "referer", referer,
     *                 "language", language
     */
@PostMapping
    public ResponseEntity<String> createLinkUnit(@RequestBody String fullLink){
    // вернёт короткую ссылку
}

}
