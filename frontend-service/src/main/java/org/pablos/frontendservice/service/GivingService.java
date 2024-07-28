package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.shortic.dto.ClickDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Service
@AllArgsConstructor
public class GivingService {

    private final RestTemplate restTemplate;
    private String givingServiceUrl;

    public String clickProcessing(final String shortLink, final HttpServletRequest request) {
        // TODO VALIDATE
        String fullLink = null;
        ClickDTO clickDTO = prepareClickDTO(shortLink, request);
        try {
            fullLink = restTemplate.postForObject(givingServiceUrl + "/click", clickDTO, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // TODO обработка ошибки 404, может другие тоже будут
                //  ввобще надо ли так, может ExceptionHandler?
            }
        }
//            если такой ссылки нет, вернуть null, т.к. выше во FrontController на этом логика есть
        return fullLink;
    }

    private ClickDTO prepareClickDTO(final String shortLink, final HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For") == null ? request.getRemoteAddr() :
                request.getHeader("X-Forwarded-For");
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        String language = request.getHeader("Accept-Language");

        return new ClickDTO(0, shortLink, LocalDateTime.now(), ipAddress, language, referer, userAgent);
    }
}
