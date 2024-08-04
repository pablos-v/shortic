package org.pablos.backendcountingservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
        ResponseEntity<?> response = restTemplate.postForEntity(givingServiceUrl, dto, FastLinkDTO.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return (FastLinkDTO) response.getBody();
        } else {
            ViolationDTO violationDTO = (ViolationDTO) response.getBody();
            String message = violationDTO == null ? "Unknown error" : violationDTO.getMessage();
            throw new SavingFastLinkException(message);
        }
    }

    @Override
    public FastLinkDTO deleteFastLink(final FastLinkDTO dto) throws DeletingFastLinkException {
        ResponseEntity<?> response = restTemplate.exchange(
                givingServiceUrl,
                HttpMethod.DELETE,
                new HttpEntity<>(dto.getShortLink()),
                FastLinkDTO.class
//                new ParameterizedTypeReference<>() {}
                );
        if (response.getStatusCode().is2xxSuccessful()) {
            return (FastLinkDTO) response.getBody();
        } else {
            ViolationDTO violationDTO = (ViolationDTO) response.getBody();
            String message = violationDTO == null ? "Unknown error" : violationDTO.getMessage();
            throw new DeletingFastLinkException(message);
        }
    }
}
