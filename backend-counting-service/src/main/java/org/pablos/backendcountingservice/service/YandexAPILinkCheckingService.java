package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.configuration.ServiceConfiguration;
import org.pablos.backendcountingservice.domain.dto.ApiRequestBody;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class YandexAPILinkCheckingService implements ILinkCheckingService {

    private final ServiceConfiguration serviceConfiguration;

    @Override
    public boolean checkLink(final String link) {
        URI url = URI.create(serviceConfiguration.getCheckingServiceUrl() + serviceConfiguration.getAPIKey(this));
        ApiRequestBody apiRequestBody = new ApiRequestBody(link);

        WebClient webClient = WebClient.create();

        ClientResponse response = webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(apiRequestBody)
                .exchange()
                .block();

        int statusCode = response.statusCode().value();
        String responseBody = response.bodyToMono(String.class).block();

        return statusCode == 200 && (responseBody == null || responseBody.length() < 3);
    }
}
