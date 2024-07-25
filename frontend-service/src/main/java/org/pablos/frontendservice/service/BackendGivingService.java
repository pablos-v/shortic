package org.pablos.frontendservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Data
@Service
public class BackendGivingService {

    private RestTemplate restTemplate;
    private String BACKEND_GIVING_SERVICE_URL;

    public String getFullLink(String shortLink) {
        String fullLink = null;
        try {
            fullLink = restTemplate.getForEntity(BACKEND_GIVING_SERVICE_URL + "/" + shortLink, String.class).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // TODO обработка ошибки 404, может другие тоже будут
            }
        }
//            если такой ссылки нет, вернуть null, т.к. выше во FrontController на этом логика есть
        return fullLink;
    }
}
