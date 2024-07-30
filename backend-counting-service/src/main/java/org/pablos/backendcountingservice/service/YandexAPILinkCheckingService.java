package org.pablos.backendcountingservice.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.dto.ApiRequestBody;
import org.pablos.shortic.dto.FastLinkDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YandexAPILinkCheckingService implements ILinkCheckingService {

    private static final String SAFE_BROWSING_URL = "https://sba.yandex.net/v4/threatMatches:find?key=";
    private final RestTemplate restTemplate;

    @Value("${api_key}")
    private String APIKey;

    @Override
    public boolean checkLink(FastLinkDTO link) {
        URI url = URI.create(SAFE_BROWSING_URL + APIKey);
        ApiRequestBody apiRequestBody = new ApiRequestBody(link.getFullLink());

        ResponseEntity<List> response = restTemplate.postForEntity(url, apiRequestBody, List.class);

        return response.getStatusCode().is2xxSuccessful()
                && (response.getBody() == null || response.getBody().isEmpty());
    }
}
