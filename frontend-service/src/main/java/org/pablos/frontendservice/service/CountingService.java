package org.pablos.frontendservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.dto.ViolationDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.WrongPasswordException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@Service
@AllArgsConstructor
public class CountingService {

    private final RestTemplate restTemplate;
    private String countingServiceUrl;

    /**
     * Посылает запрос на создание ссылки.
     * @param input
     * @return
     */
    public FastLinkDTO createLink(final FastLinkDTO input) {
        return restTemplate.postForObject(countingServiceUrl + "/link", input, FastLinkDTO.class);
    }

    public LinkUnitDTO getLinkUnit(LinkUnitDTO dto) {
        ResponseEntity<?> response = restTemplate.exchange(
                countingServiceUrl + "/link",
                HttpMethod.GET,
                new HttpEntity<>(dto),
                LinkUnitDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (LinkUnitDTO) response.getBody();
        } else if (response.getStatusCode().is4xxClientError()) {
            ViolationDTO violationDTO = (ViolationDTO) response.getBody();
            if (violationDTO != null && violationDTO.getFieldName().equals("password")) {
                throw new WrongPasswordException();
            }
        }
        throw new LinkNotFoundException();
    }
}
