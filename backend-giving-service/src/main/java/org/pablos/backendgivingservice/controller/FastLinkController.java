package org.pablos.backendgivingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.pablos.backendgivingservice.domain.entity.FastLink;
import org.pablos.backendgivingservice.service.ICountingServiceClient;
import org.pablos.backendgivingservice.service.IFastLinkService;
import org.pablos.shortic.dto.ClickDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO check http://localhost:18081/swagger-ui/index.html
 * REST контроллер для работы с сервисом.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/")
@Tag(name = "backend_giving_service")
public class FastLinkController {

    private final IFastLinkService service;
    private final ICountingServiceClient countingService;

//    /**
//     * Отдаёт полную ссылку, соответствующую короткой ссылке в запросе.
//     * @param link короткая ссылка
//     * @return полная ссылка
//     */
//    @Operation(summary = "Returns the full link corresponding to the short link in the request.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Success"),
//            @ApiResponse(responseCode = "400", description = "Bad request if short link is incorrect"),
//            @ApiResponse(responseCode = "404", description = "Link not found")
//    })
//    @GetMapping("{link}")
//    public ResponseEntity<String> getFullLink(@PathVariable final String link) {
//        return ResponseEntity.ok(service.getFullLink(link.trim()));
//    }

    /**
     * Обрабатывает клик по ссылке: отправляет данные клика для записи статистики клика и отдаёт полную ссылку.
     * @param clickDTO DTO клика
     * @return полная ссылка
     */
    @Operation(summary = "Sends click statistics to counting service " +
            "and returns the full link corresponding to the short link in the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request if short link is incorrect"),
            @ApiResponse(responseCode = "404", description = "Link not found")
    })
    @PostMapping("click")
    public ResponseEntity<String> clickProcessing(@RequestBody final ClickDTO clickDTO){

        // отправка статистики клика
        new Thread(() -> countingService.postStatistics(clickDTO)).start();

        return ResponseEntity.ok(service.getFullLink(clickDTO.getShortLink().trim()));
    }

    /**
     * Создаёт объект {@link FastLink} на основе переданного DTO
     * @param fastLink объект DTO, содержащий короткую и полную ссылки
     * @return {@link org.pablos.shortic.dto.FastLinkDTO} DTO созданного объекта
     */
    @Operation(summary = "Creates a new FastLink object in DB, based on the provided DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400",
                    description = "Bad request if DTO was not provided or short link is incorrect")
    })
    @PostMapping
    public ResponseEntity<org.pablos.shortic.dto.FastLinkDTO> createFastLink(@RequestBody final org.pablos.shortic.dto.FastLinkDTO fastLink) {
        return ResponseEntity.ok(service.create(fastLink));
    }

    /**
     * Обновляет объект {@link FastLink} на основе переданного DTO
     * @param fastLink объект DTO, содержащий короткую и обновлённую полную ссылки
     * @return {@link org.pablos.shortic.dto.FastLinkDTO} DTO измененного объекта
     */
    @Operation(summary = "Updates a FastLink object in DB, basing on the provided DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400",
                    description = "Bad request if DTO was not provided or short link is incorrect"),
            @ApiResponse(responseCode = "404", description = "Object not found")
    })
    @PutMapping
    public ResponseEntity<org.pablos.shortic.dto.FastLinkDTO> updateFastLink(@RequestBody final org.pablos.shortic.dto.FastLinkDTO fastLink) {
        return ResponseEntity.ok(service.update(fastLink));
    }

    /**
     * Удаляет объект {@link FastLink} из БД
     * @param shortLink Короткая ссылка, она же ID удаляемого объекта {@link FastLink}
     * @return {@link org.pablos.shortic.dto.FastLinkDTO} DTO удалённого объекта
     */
    @Operation(summary = "Deletes a FastLink object from DB, according to a short link, which is ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Link not found")
    })
    @DeleteMapping
    public ResponseEntity<org.pablos.shortic.dto.FastLinkDTO> deleteFastLink(@RequestBody final String shortLink) {
        return ResponseEntity.ok(service.deleteByShortLink(shortLink));
    }

}