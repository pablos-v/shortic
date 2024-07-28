package org.pablos.backendgivingservice.configuration;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 */
@Configuration
@RequiredArgsConstructor
public class GivingServiceConfiguration {

    private final EurekaClient eurekaClient;

    @Bean
    public String countingServiceUrl(){
        return  getBackendIp("BACKEND-COUNTING-SERVICE");
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
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
}
