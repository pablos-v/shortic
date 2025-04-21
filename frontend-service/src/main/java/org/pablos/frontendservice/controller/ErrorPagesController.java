package org.pablos.frontendservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки ошибок.
 */
@Controller
public class ErrorPagesController {

    @GetMapping("/error/404")
    public String notFound(){
        return "error/404";
    }

    @GetMapping("/error/400")
    public String wrongInput(){
        return "error/400";
    }

    @GetMapping("/error/500")
    public String serverError(){
        return "error/500";
    }

    @GetMapping("/error/password")
    public String wrongPassword(){
        return "error/password";
    }
}
