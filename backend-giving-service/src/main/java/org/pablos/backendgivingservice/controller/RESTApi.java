package org.pablos.backendgivingservice.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTApi {

    @Value("${properties.shortLinkLength}")
    private int linkLengthForValidation;

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