package org.pablos.frontendservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.shortic.dto.FastLinkDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@Service
@AllArgsConstructor
public class CountingService {

    private final RestTemplate restTemplate;
    private String countingServiceUrl;

    public FastLinkDTO getLink(final FastLinkDTO input) {
        return restTemplate.postForObject(countingServiceUrl + "/link", input, FastLinkDTO.class);
    }
}
