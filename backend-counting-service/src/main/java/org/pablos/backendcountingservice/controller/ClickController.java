package org.pablos.backendcountingservice.controller;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.service.IClickService;
import org.pablos.common.dto.ClickDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с кликами.
 */
@RestController
@RequestMapping("/click")
@RequiredArgsConstructor
public class ClickController {

    private final IClickService clickService;

    /**
     * Принимает объект с данными клика и сохраняет в БД
     */
    @PostMapping
    public ResponseEntity<Void> createClick (@RequestBody ClickDTO clickDTO){
        clickService.createClick(clickDTO);
        return ResponseEntity.ok().build();
    }


}
