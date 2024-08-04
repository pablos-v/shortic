package org.pablos.backendcountingservice.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.configuration.ServiceConfiguration;
import org.pablos.backendcountingservice.domain.dto.ApiRequestBody;
import org.pablos.shortic.dto.FastLinkDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YandexAPILinkCheckingService implements ILinkCheckingService {

    private final ServiceConfiguration serviceConfiguration;
    private final RestTemplate restTemplate;

    @Override
    public boolean checkLink(final String link) {
        URI url = URI.create(serviceConfiguration.checkingServiceUrl + serviceConfiguration.getAPIKey(this));
        ApiRequestBody apiRequestBody = new ApiRequestBody(link);

        ResponseEntity<List> response = restTemplate.postForEntity(url, apiRequestBody, List.class);

        return response.getStatusCode().is2xxSuccessful()
                && (response.getBody() == null || response.getBody().isEmpty());
    }
}
