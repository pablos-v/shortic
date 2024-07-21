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
/**
 * контроллер входящий клик PathVariable
 * TODO валидация входных данных клика, не более Х символов (регулярного выражения)
 *  Х брать из конфига Клауд
 *  @ExceptionHandler
 * данные клика отдельным потоком в каунтингСервис
 * в сервис, там @Transactional @Cacheable
 * Запрос из репо
 *  @ExceptionHandler
 * DTO похоже и не надо, удалить из DTO-storage
 * отдать полную ссылку
 *
 */