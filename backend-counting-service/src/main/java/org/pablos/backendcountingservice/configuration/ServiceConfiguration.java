package org.pablos.backendcountingservice.configuration;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.service.ILinkCheckingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация сервиса
 */
@Configuration
@RequiredArgsConstructor
public class ServiceConfiguration {

    /**
     * Количество потоков
     */
    @Getter
    @Value("${properties.threads_number}")
    private int numberOfThreads;

    /**
     * Лимит времени для проверки
     */
    @Getter
    @Value("${properties.checking_length}")
    private int timeLimitForCheckInMinutes;

    /**
     * URL сервиса проверки
     */
    @Getter
    @Value("${properties.checking_service_url}")
    private String checkingServiceUrl;

    /**
     * API ключ
     */
    @Value("${properties.checking_api_key}")
    private String APIKey;

    /**
     * Клиент Eureka
     */
    private final EurekaClient eurekaClient;

    /**
     * URL сервиса отдачи ссылок
     */
    @Bean
    public String givingServiceUrl(){
        return getBackendIp("BACKEND-GIVING-SERVICE");
    }

    /**
     * RestTemplate для выполнения HTTP-запросов
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    /**
     * Логгер для записи сообщений
     */
    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    /**
     * Вытаскивает из эврики адрес бэкенда
     *
     * @param app имя сервиса
     * @return IP адрес запрошенного сервиса
     */
    private String getBackendIp(String app) {
        InstanceInfo info = eurekaClient.getApplication(app).getInstances().get(0);
        return "http://" + info.getIPAddr() + ":" + info.getPort();
    }

    /**
     * Возвращает API ключ, если запрашивающий является сервисом проверки ссылок
     *
     * @param asking кто запрашивает API ключ?
     * @return API ключ или null, если запрашивающий не является сервисом проверки ссылок
     */
    public String getAPIKey(Object asking) {
        return asking instanceof ILinkCheckingService ? APIKey : null;
    }
}
