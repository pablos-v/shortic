package org.pablos.frontendservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.frontendservice.exception.WrongInputException;
import org.pablos.frontendservice.service.ICountingService;
import org.pablos.frontendservice.service.IGivingService;
import org.pablos.shortic.dto.ClickDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Service
@AllArgsConstructor
public class GivingService implements IGivingService {

    private final RestTemplate restTemplate;
    private final org.pablos.frontendservice.service.ICountingService ICountingService;
    private final String givingServiceUrl;

    @Override
    public String clickProcessing(final String shortLink, final HttpServletRequest request) throws LinkNotFoundException,
            WrongInputException, LinkProcessingException {

        CommonUtil.validateShortLink(shortLink);

        // отправка статистики клика
        new Thread(() -> postStatistics(shortLink, request)).start();

        try {
            return restTemplate.getForObject(givingServiceUrl + "/click/" + shortLink, String.class);
        } catch (HttpClientErrorException e) {
            ViolationDTO responseBody = e.getResponseBodyAs(ViolationDTO.class);
            if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))){
                throw new WrongInputException(Objects.requireNonNullElse(responseBody, "Неизвестная ошибка").toString());
            } else if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))){
                throw new LinkNotFoundException(Objects.requireNonNullElse(responseBody, "Неизвестная ошибка").toString());
            }
                throw new WrongInputException("Неизвестная ошибка");
        }
    }

    private void postStatistics(final String shortLink, final HttpServletRequest request){
        ClickDTO clickDTO = prepareClickDTO(shortLink, request);
        ICountingService.postStatistics(clickDTO);
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
