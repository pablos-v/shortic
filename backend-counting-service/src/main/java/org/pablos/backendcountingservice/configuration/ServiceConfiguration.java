package org.pablos.backendcountingservice.configuration;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.pablos.backendcountingservice.service.ILinkCheckingService;
import org.pablos.backendcountingservice.service.YandexAPILinkCheckingService;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 */
@Configuration
@RequiredArgsConstructor
public class ServiceConfiguration {

    @Value("${threads_number")
    public int numberOfThreads;

    @Value("${checking_length")
    public int timeLimitForCheckInMinutes;

    @Value("${checking_service_url")
    public int checkingServiceUrl;

    @Value("${checking_api_key}")
    private String APIKey;

    private final EurekaClient eurekaClient;

    @Bean
    public String givingServiceUrl(){
        return getBackendIp("BACKEND-GIVING-SERVICE");
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    /**
     * Вытаскивает из эврики адрес бэкенда
     *
     * @return IP адрес бэкенда
     */
    private String getBackendIp(String app) {
        InstanceInfo info = eurekaClient.getApplication(app).getInstances().get(0);
        return "http://" + info.getIPAddr() + ":" + info.getPort();
    }

    public String getAPIKey(Object asking) {
        if (asking instanceof ILinkCheckingService){
            return APIKey;
        } else {
            return null;
        }
    }
}
