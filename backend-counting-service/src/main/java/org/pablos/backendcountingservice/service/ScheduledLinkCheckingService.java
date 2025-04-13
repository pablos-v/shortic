package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.configuration.ServiceConfiguration;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.service.mapper.LinkUnitMapper;
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

    // Периодичность постоянной проверки ссылок
    public static final String CRON_PERIOD = "0 0 3 * * ?"; // ежедневно в 3 утра

    private final ServiceConfiguration serviceConfiguration;

    private final ILinkUnitService linkUnitService;

    /**
     * Метод, который запускается каждый день в 3 утра и проверяет все ссылки на безопасность.
     * Использует ExecutorService для параллельного выполнения задач.
     */
    @Scheduled(cron = CRON_PERIOD)
    private void checkAllLinksByPeriod() {
        ExecutorService service = Executors.newFixedThreadPool(serviceConfiguration.getNumberOfThreads());
        List<LinkUnit> linkUnits = linkUnitService.getAllLinks();
        try {
            // Отправляем задачи на выполнение
            for (LinkUnit linkUnit : linkUnits) {
                service.submit(() -> linkUnitService.checkExistingLinkSecurity(LinkUnitMapper.toDto(linkUnit)));
            }
            service.awaitTermination(serviceConfiguration.getTimeLimitForCheckInMinutes(), TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            service.shutdownNow(); // Принудительное завершение при прерывании
            Thread.currentThread().interrupt(); // Восстановление статуса прерывания
        } finally {
            service.shutdown(); // Завершаем ExecutorService
        }
    }

}
