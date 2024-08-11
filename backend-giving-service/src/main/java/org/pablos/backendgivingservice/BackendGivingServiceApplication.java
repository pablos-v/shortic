package org.pablos.backendgivingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Основное приложение spring boot, которое запустит веб-контейнер и подключит все необходимые компоненты.
 */
@EnableDiscoveryClient
@SpringBootApplication
//@EnableCaching TODO
public class BackendGivingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendGivingServiceApplication.class, args);
    }

}

// TODO ExceptionHandlingService в бекендах - повторяющийся код как убрать?
// TODO мапперы переписать на MapSctruct