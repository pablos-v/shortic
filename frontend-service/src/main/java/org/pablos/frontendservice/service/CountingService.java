package org.pablos.frontendservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.frontendservice.exception.WrongInputException;
import org.pablos.shortic.dto.*;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkNotSecureException;
import org.pablos.shortic.exception.WrongPasswordException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
    public LinkUnitDTO createLink(final FastLinkDTO input) throws WrongInputException {
        ResponseEntity<?> response = restTemplate.postForEntity(
                countingServiceUrl + "/link",
                input,
                LinkUnitDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (LinkUnitDTO) response.getBody();
        } else throw new WrongInputException();
    }

    public PageDTO getPageOfClicks(int page, int size, LinkUnitDTO dto) throws WrongPasswordException, LinkNotFoundException {
        ResponseEntity<?> response = restTemplate.exchange(
                countingServiceUrl + "/link",
                HttpMethod.GET,
                new HttpEntity<>(new PageRequestDTO(page, size, dto)),
                PageDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (PageDTO) response.getBody();
        } else if (response.getStatusCode().is4xxClientError()) {
            ViolationDTO violationDTO = (ViolationDTO) response.getBody();
            if (violationDTO != null && violationDTO.getFieldName().equals("password")) {
                throw new WrongPasswordException();
            }
        }
        throw new LinkNotFoundException();
    }

    public PageDTO updateAndGetPageOfClicks(int page, int size, LinkUnitDTO dto) throws LinkNotSecureException, WrongInputException {
        ResponseEntity<?> response = restTemplate.exchange(
                countingServiceUrl + "/link",
                HttpMethod.PUT,
                new HttpEntity<>(new PageRequestDTO(page, size, dto)),
                PageDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (PageDTO) response.getBody();
        } else if (response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(410))){
            throw new LinkNotSecureException();
        } else {
            throw new WrongInputException();
        }
    }

    public void postStatistics(ClickDTO clickDTO) {
        restTemplate.postForLocation(countingServiceUrl + "/click", clickDTO);
    }
}
