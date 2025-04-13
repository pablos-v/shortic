package org.pablos.backendcountingservice.service;

/**
 * Интерфейс сервиса для проверки ссылок.
 */
public interface ILinkCheckingService {

    /**
     * Проверяет ссылку на наличие потенциальных угроз.
     *
     * @param link ссылка, которую нужно проверить.
     * @return true, если ссылка безопасна, false - если ссылка содержит потенциальные угрозы.
     */
    boolean checkLink(final String link);
}

