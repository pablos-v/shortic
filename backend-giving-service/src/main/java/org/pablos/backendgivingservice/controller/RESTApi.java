package org.pablos.backendgivingservice.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.backendgivingservice.service.FastLinkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * TODO валидация входных данных клика, не более Х символов (регулярного выражения)
 *  Х брать из конфига Клауд
 *  @ExceptionHandler
 * данные клика отдельным потоком в каунтингСервис
 * в сервис, там @Transactional @Cacheable
 * Запрос из репо
 *  @ExceptionHandler
 * DTO похоже и не надо, удалить из DTO-storage
 * отдать полную ссылку
 *
 */
@Data
@RestController
@RequestMapping("/")
public class RESTApi {

    @Value("${properties.shortLinkLength}")
    private int linkLengthForValidation;

    private FastLinkService service;

    @GetMapping("{link}")
    public ResponseEntity<String> getLink(@PathVariable String link) {
        return ResponseEntity.ok(service.getFullLink(link));
    }
}
/**
 * TODO
 *  * GET
 *  * стирание ссылки
 *  * запись ссылки
 *  * изменение ссылки
 *
 *  http://localhost:18081/swagger-ui/index.html
 *  расписать все ручки как в https://github.com/pablos-v/prbank_test_task/blob/master/src/main/java/ru/prbank/test_task/seacombat/api/MainController.java
 */