package org.pablos.frontendservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.pablos.frontendservice.service.BackendGivingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;

@Data
@Controller
public class FrontController {

    private BackendGivingService givingService;

    @GetMapping("{link}")
    public RedirectView getLink(@PathVariable String link, HttpServletRequest request){
        String fullLink = givingService.getFullLink(link);
// TODO тут создать поток , чтобы он вызывал BackendCountingService.putStatistics(request)
        // как передать в поток request? или он сам отсюда возьмёт?

        // Обработка ситуации, когда сокращенная ссылка не найдена
        return new RedirectView(Objects.requireNonNullElse(fullLink, "/404"));
    }

}
