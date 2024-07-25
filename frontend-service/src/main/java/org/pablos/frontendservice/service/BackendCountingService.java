package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Service
public class BackendCountingService {

    private RestTemplate restTemplate;
    private String BACKEND_COUNTING_SERVICE_URL;

    public void putStatistics(String link, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For") == null ? request.getRemoteAddr() :
                request.getHeader("X-Forwarded-For");
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        String language = request.getHeader("Accept-Language");

        Map<String, String> body = Map.of(
                "link", link,
                "ipAddress", ipAddress,
                "userAgent", userAgent,
                "referer", referer,
                "language", language
        );

        restTemplate.put(BACKEND_COUNTING_SERVICE_URL, body);
    }

}
