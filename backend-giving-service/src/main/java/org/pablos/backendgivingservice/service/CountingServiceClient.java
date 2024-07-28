package org.pablos.backendgivingservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pablos.shortic.dto.ClickDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@Service
@AllArgsConstructor
public class CountingServiceClient implements ICountingServiceClient {

    private final RestTemplate restTemplate;
    private String countingServiceUrl;

    @Override
    public void postStatistics(final ClickDTO clickDTO) {
        restTemplate.postForLocation(countingServiceUrl + "/click", clickDTO);
    }

}
