package org.pablos.backendcountingservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.UpdatingFastLinkException;
import org.pablos.backendcountingservice.service.IGivingServiceClient;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Data
@Service
@AllArgsConstructor
public class GivingServiceClient implements IGivingServiceClient {

    private final RestTemplate restTemplate;
    private String givingServiceUrl;


    @Override
    public FastLinkDTO saveFastLink(final FastLinkDTO dto) throws SavingFastLinkException {
        try {
            ResponseEntity<FastLinkDTO> response = restTemplate.postForEntity(givingServiceUrl, dto, FastLinkDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new SavingFastLinkException(e.getResponseBodyAsString());
        }
    }

    @Override
    public FastLinkDTO deleteFastLink(final FastLinkDTO dto) throws DeletingFastLinkException {
        try {
            ResponseEntity<FastLinkDTO> response = restTemplate.exchange(givingServiceUrl, HttpMethod.DELETE,
                    new HttpEntity<>(dto.getShortLink()), FastLinkDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new DeletingFastLinkException(e.getResponseBodyAsString());
        }
    }

    @Override
    public void updateFastLink(FastLinkDTO fastLinkDTO) {
        try {
            restTemplate.exchange(givingServiceUrl, HttpMethod.PUT, new HttpEntity<>(fastLinkDTO), Void.class);
        } catch (HttpClientErrorException e) {
            throw new UpdatingFastLinkException(e.getResponseBodyAsString());
        }
    }
}
