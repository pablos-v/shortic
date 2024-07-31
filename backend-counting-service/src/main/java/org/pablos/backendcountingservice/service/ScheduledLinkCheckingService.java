package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.shortic.dto.FastLinkDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ScheduledLinkCheckingService {

    @Value("${threads_number")
    private int numberOfThreads;

    @Value("${checking_length")
    private int timeLimitForCheckInMinutes;

    private final LinkUnitService linkUnitService;

    @Scheduled(cron = "0 0 3 * * ?") // Каждый день в 3 утра
    private void checkAllLinksByPeriod() {
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        List<FastLinkDTO> linkUnits = linkUnitService.getAllLinks();
        try {
            // Отправляем задачи на выполнение
            for (FastLinkDTO linkUnit : linkUnits) {
                service.submit(() -> linkUnitService.checkExistingLinkSecurity(linkUnit));
            }
            service.awaitTermination(timeLimitForCheckInMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            service.shutdownNow(); // Принудительное завершение при прерывании
            Thread.currentThread().interrupt(); // Восстановление статуса прерывания
        } finally {
            service.shutdown(); // Завершаем ExecutorService
        }
    }

}
