package org.pablos.backendgivingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Основной класс модуля, который быстро отдаёт полные ссылки по запросу. Использует кэширование.
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableCaching
public class BackendGivingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendGivingServiceApplication.class, args);
    }

}
