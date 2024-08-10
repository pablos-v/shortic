package org.pablos.frontendservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.frontendservice.exception.WrongInputException;
import org.pablos.shortic.dto.*;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkNotSecureException;
import org.pablos.shortic.exception.PasswordIncorrectException;
import org.pablos.shortic.exception.WrongPasswordException;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
        try {
            ResponseEntity<LinkUnitDTO> response = restTemplate.postForEntity(
                    countingServiceUrl + "/link",
                    input,
                    LinkUnitDTO.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            ViolationDTO violationDTO = e.getResponseBodyAs(ViolationDTO.class);
            throw new WrongInputException(Objects.requireNonNullElse(violationDTO, "Unknown error").toString());
        }
        throw new WrongInputException("Unknown error");
    }

    public PageDTO getPageOfClicks(int page, int size, String shortLink, String password) throws WrongInputException,
            WrongPasswordException, LinkNotFoundException {
        try {
            String url = countingServiceUrl + "/link" + "?page=" + page + "&size=" + size + "&shortLink="
                    + shortLink + "&password=" + CommonUtil.encodePassword(password);
            ResponseEntity<PageDTO> response = restTemplate.getForEntity(url, PageDTO.class);

            if (response.getStatusCode().is2xxSuccessful()) {

                PageDTO responseBody = response.getBody();
                if (responseBody != null) {
                    String passwordDecoded = CommonUtil.decodePassword(responseBody.getLinkUnit().getPassword());
                    responseBody.getLinkUnit().setPassword(passwordDecoded);
                }
                return responseBody;
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

    public void postStatistics(ClickDTO clickDTO) {
        restTemplate.postForLocation(countingServiceUrl + "/click", clickDTO);
    }

    public void updateLink(String shortLink, String fullLink) throws LinkNotSecureException, WrongInputException {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    countingServiceUrl + "/link" + "?shortLink=" + shortLink + "&fullLink=" + fullLink,
                    HttpMethod.PUT,
                    null,
                    Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(410))){
                throw new LinkNotSecureException();
            } else {
                ViolationDTO responseBody = e.getResponseBodyAs(ViolationDTO.class);
                throw new WrongInputException(Objects.requireNonNullElse(responseBody, "Неизвестная ошибка").toString());
            }
        }
    }

    public void setPassword(String shortLink, String password) {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                        countingServiceUrl + "/link/password" + "?shortLink=" + shortLink
                                + "&password=" + CommonUtil.encodePassword(password),
                        HttpMethod.PUT,
                        null,
                        Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            }
        } catch (HttpClientErrorException e) {
            ViolationDTO responseBody = e.getResponseBodyAs(ViolationDTO.class);
            if (responseBody!=null && e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))
            && responseBody.getFieldName().equals("password")) {
                throw new PasswordIncorrectException();
            }
            throw new WrongInputException();
        }
    }
}
