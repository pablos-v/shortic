package org.pablos.backendgivingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Основное приложение spring boot, которое запустит веб-контейнер и подключит все необходимые компоненты.
 */
@SpringBootApplication
public class BackendGivingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendGivingServiceApplication.class, args);
    }

}
