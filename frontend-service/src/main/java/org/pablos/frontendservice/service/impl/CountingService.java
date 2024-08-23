package org.pablos.frontendservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.frontendservice.service.ICountingService;
import org.pablos.common.dto.ClickDTO;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.dto.LinkUnitDTO;
import org.pablos.common.dto.PageDTO;
import org.pablos.common.exception.*;
import org.pablos.common.util.CommonUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Data
@Service
@AllArgsConstructor
public class CountingService implements ICountingService {

    private final RestTemplate restTemplate;
    private String countingServiceUrl;

    @Override
    public LinkUnitDTO createLink(final FastLinkDTO input) throws WrongInputException, FullLinkNotProvidedException,
            FullLinkSizeException, FullLinkFormatException {
        CommonUtil.validateDTOFullLink(input);
        String message = "Unknown error";
        try {
            ResponseEntity<LinkUnitDTO> response = restTemplate.postForEntity(
                    countingServiceUrl + "/link",
                    input,
                    LinkUnitDTO.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            message = e.getResponseBodyAsString();
        }
        throw new WrongInputException(message);
    }

    @Override
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
                throw new LinkNotFoundException(e.getResponseBodyAsString());
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new WrongInputException(e.getResponseBodyAsString());
            }
        }
        throw new LinkNotFoundException();
    }

    @Override
    public void postStatistics(ClickDTO clickDTO) {
        restTemplate.postForLocation(countingServiceUrl + "/click", clickDTO);
    }

    @Override
    public void updateLink(String shortLink, String fullLink) throws LinkNotSecureException, WrongInputException,
            FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException {
        CommonUtil.validateFullLink(fullLink);
        try {
            restTemplate.exchange(
                    countingServiceUrl + "/link" + "?shortLink=" + shortLink + "&fullLink=" + fullLink,
                    HttpMethod.PUT,
                    null,
                    Void.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(410))){
                throw new LinkNotSecureException();
            } else {
                throw new WrongInputException(e.getResponseBodyAsString());
            }
        }
    }

    @Override
    public void setPassword(String shortLink, String password) {
        try {
            restTemplate.exchange(
                        countingServiceUrl + "/link/password" + "?shortLink=" + shortLink
                                + "&password=" + CommonUtil.encodePassword(password),
                        HttpMethod.PUT,
                        null,
                        Void.class);
        } catch (HttpClientErrorException e) {
            throw new WrongInputException(e.getResponseBodyAsString());
        }
    }
}
