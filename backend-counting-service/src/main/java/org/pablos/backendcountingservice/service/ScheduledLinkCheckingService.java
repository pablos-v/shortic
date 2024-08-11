package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.configuration.ServiceConfiguration;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Сервис периодической проверки ссылок
 */
@Service
@RequiredArgsConstructor
public class ScheduledLinkCheckingService {

    private final ServiceConfiguration serviceConfiguration;

    private final LinkUnitService linkUnitService;

    @Scheduled(cron = "0 0 3 * * ?") // Каждый день в 3 утра
    private void checkAllLinksByPeriod() {
        ExecutorService service = Executors.newFixedThreadPool(serviceConfiguration.numberOfThreads);
        List<LinkUnit> linkUnits = linkUnitService.getAllLinks();
        try {
            // Отправляем задачи на выполнение
            for (LinkUnit linkUnit : linkUnits) {
                service.submit(() -> linkUnitService.checkExistingLinkSecurity(linkUnit));
            }
            service.awaitTermination(serviceConfiguration.timeLimitForCheckInMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            service.shutdownNow(); // Принудительное завершение при прерывании
            Thread.currentThread().interrupt(); // Восстановление статуса прерывания
        } finally {
            service.shutdown(); // Завершаем ExecutorService
        }
    }

}
