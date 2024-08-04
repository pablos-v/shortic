package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.shortic.dto.ClickDTO;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Data
@Service
@AllArgsConstructor
public class GivingService {

    private final RestTemplate restTemplate;
    private final CountingService countingService;
    private String givingServiceUrl;

    /**
     * Обрабатывает клик по ссылке: отправляет данные клика для записи статистики клика
     * @param shortLink
     * @param request
     * @return
     */
    public String clickProcessing(final String shortLink, final HttpServletRequest request) {
        CommonUtil.validateDTOShortLink(new FastLinkDTO(shortLink,""));

        // отправка статистики клика
        new Thread(() -> postStatistics(shortLink, request)).start();

        String fullLink = null;
        try {
            fullLink = restTemplate.getForObject(givingServiceUrl + "/click/" + shortLink, String.class);
        } catch (Exception e) {
                // TODO обработка ошибки 404, может другие тоже будут
                //  ввобще надо ли так, может ExceptionHandler?
        }
//            если такой ссылки нет, вернуть null, т.к. выше во FrontController на этом логика есть
        return fullLink;
    }

    private void postStatistics(final String shortLink, final HttpServletRequest request){
        ClickDTO clickDTO = prepareClickDTO(shortLink, request);
        countingService.postStatistics(clickDTO);
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
