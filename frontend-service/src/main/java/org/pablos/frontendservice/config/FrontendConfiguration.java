package org.pablos.frontendservice.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pablos.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Конфигурация фронтенд-сервиса.
 */
@Configuration
@RequiredArgsConstructor
public class FrontendConfiguration {

    /**
     * Длина сокращённой ссылки.
     */
    @Getter
    @Value("${properties.short_link_length}")
    private int shortLinkLength;

    /**
     * Максимальная длина сокращаемой ссылки.
     */
    @Getter
    @Value("${properties.full_link_max_length}")
    private int fullLinkMaxLength;

    /**
     * Юридические данные для подстановки на страницу oferta.html и privacy.html
     */
    @Getter
    @Value("${properties.from_who}")
    private String fromWho;

    private final EurekaClient eurekaClient;

    private String serverUrl;

    /**
     * URL сервиса подсчёта статистики.
     */
    @Bean
    public String countingServiceUrl() {
        return getBackendURL("BACKEND-COUNTING-SERVICE");
    }

    /**
     * URL сервиса отдачи ссылок.
     */
    @Bean
    public String givingServiceUrl() {
        return getBackendURL("BACKEND-GIVING-SERVICE");
    }

    /**
     * Возвращает RestTemplate для выполнения HTTP-запросов.
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    /**
     * Возвращает логгер для записи сообщений.
     */
    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    /**
     * Предоставляет адрес текущего сервера, готовый для подстановки - со слешем "/" на конце.
     */
    public String getServerUrl() {
        if (serverUrl == null) {
            serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/";
        }
        return serverUrl;
    }

    /**
     * Вытаскивает из эврики адрес бэкенда
     *
     * @param app имя сервиса
     * @return URL запрошенного сервиса
     */
    private String getBackendURL(String app) {
        InstanceInfo info = eurekaClient.getApplication(app).getInstances().get(0);
        return "http://" + info.getIPAddr() + ":" + info.getPort();
    }

    /**
     * Инициализация длин ссылок.
     */
    @PostConstruct
    public void init() {
        CommonUtil.FULL_LINK_MAX_LENGTH = fullLinkMaxLength;
        CommonUtil.SHORT_LINK_LENGTH = shortLinkLength;
    }

}

