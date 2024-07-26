package org.pablos.backendcountingservice.controller;

import lombok.RequiredArgsConstructor;
import org.pablos.shortic.dto.ClickDTO;
import org.pablos.backendcountingservice.service.ClickService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ClickController {

    private final ClickService clickService;

    @PostMapping
    public ResponseEntity<?> createClick (@RequestBody ClickDTO click){
        clickService.createClick(click);
        return ResponseEntity.ok().build();
    }


}
