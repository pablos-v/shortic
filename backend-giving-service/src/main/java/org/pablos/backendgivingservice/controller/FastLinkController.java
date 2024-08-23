package org.pablos.backendgivingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pablos.backendgivingservice.entity.FastLink;
import org.pablos.backendgivingservice.service.IFastLinkService;
import org.pablos.common.dto.FastLinkDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для работы с сервисом.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/")
@Tag(name = "backend_giving_service")
public class FastLinkController {

    private final IFastLinkService service;

    /**
     * Отдаёт полную ссылку.
     * @param shortLink короткая ссылка
     * @return полная ссылка
     */
    @Operation(summary = "Sends click statistics to counting service " +
            "and returns the full link corresponding to the short link in the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request if short link is incorrect"),
            @ApiResponse(responseCode = "404", description = "Link not found")
    })
    @GetMapping("click/{shortLink}")
    public ResponseEntity<String> getFullLink(@PathVariable final String shortLink){

        String fullLink = service.getFullLink(shortLink.trim());
        
        return ResponseEntity.ok(fullLink);
    }

    /**
     * Создаёт объект {@link FastLink} на основе переданного DTO
     * @param fastLink объект DTO, содержащий короткую и полную ссылки
     * @return {@link FastLinkDTO} DTO созданного объекта
     */
    @Operation(summary = "Creates a new FastLink object in DB, based on the provided DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400",
                    description = "Bad request if DTO was not provided or short link is incorrect")
    })
    @PostMapping
    public ResponseEntity<FastLinkDTO> createFastLink(@RequestBody final FastLinkDTO fastLink) {
        return ResponseEntity.ok(service.create(fastLink));
    }

    /**
     * Обновляет объект {@link FastLink} на основе переданного DTO
     * @param fastLink объект DTO, содержащий короткую и обновлённую полную ссылки
     * @return {@link FastLinkDTO} DTO измененного объекта
     */
    @Operation(summary = "Updates a FastLink object in DB, basing on the provided DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400",
                    description = "Bad request if DTO was not provided or short link is incorrect"),
            @ApiResponse(responseCode = "404", description = "Link not found")
    })
    @PutMapping
    public ResponseEntity<FastLinkDTO> updateFastLink(@RequestBody final FastLinkDTO fastLink) {
        return ResponseEntity.ok(service.update(fastLink));
    }

    /**
     * Удаляет объект {@link FastLink} из БД
     * @param shortLink Короткая ссылка, она же ID удаляемого объекта {@link FastLink}
     * @return {@link FastLinkDTO} DTO удалённого объекта
     */
    @Operation(summary = "Deletes a FastLink object from DB, according to a short link, which is ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Link not found")
    })
    @DeleteMapping
    public ResponseEntity<FastLinkDTO> deleteFastLink(@RequestBody final String shortLink) {
        return ResponseEntity.ok(service.deleteByShortLink(shortLink));
    }

}