package org.pablos.frontendservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.frontendservice.exception.WrongInputException;
import org.pablos.shortic.dto.*;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkNotSecureException;
import org.pablos.shortic.exception.WrongPasswordException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

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

    public PageDTO getPageOfClicks(int page, int size, String shortLink, String password) throws WrongInputException, WrongPasswordException, LinkNotFoundException {
        try {
//            ResponseEntity<PageDTO> response = restTemplate.exchange(
//                    countingServiceUrl + "/link",
//                    HttpMethod.GET,
//                    new HttpEntity<>(new PageRequestDTO(page, size, dto)),
//                    PageDTO.class);
            String url = countingServiceUrl + "/link" + "?page=" + page + "&size=" + size + "&shortLink=" + shortLink + "&password=" + password;
            ResponseEntity<PageDTO> response = restTemplate.getForEntity(url, PageDTO.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new WrongPasswordException();
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new LinkNotFoundException();
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                ViolationDTO responseBody = e.getResponseBodyAs(ViolationDTO.class);
                throw new WrongInputException(Objects.requireNonNullElse(responseBody, "Неизвестная ошибка").toString());
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
