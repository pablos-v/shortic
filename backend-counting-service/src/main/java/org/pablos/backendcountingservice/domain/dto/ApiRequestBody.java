package org.pablos.backendcountingservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс, представляющий тело запроса к API.
 */
@Data
public class ApiRequestBody {

    /**
     * Информация о клиенте.
     */
    private Client client;

    /**
     * Информация о потенциальной угрозе.
     */
    private ThreatInfo threatInfo;

    /**
     * Конструктор класса.
     *
     * @param link ссылка, которую нужно проверить.
     */
    public ApiRequestBody(String link) {
        this.client = new Client("shortic", "1.0");
        this.threatInfo = new ThreatInfo(
                new String[]{"MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE", "POTENTIALLY_HARMFUL_APPLICATION"},
                new String[]{"ALL_PLATFORMS"},
                new String[]{"URL", "EXECUTABLE"},
                new ThreatEntry[]{new ThreatEntry(link)}
        );
    }

    /**
     * Класс, представляющий информацию о клиенте.
     */
    @Data
    @AllArgsConstructor
    public static class Client {
        /**
         * Идентификатор клиента.
         */
        private String clientId;

        /**
         * Версия клиента.
         */
        private String clientVersion;
    }

    /**
     * Класс, представляющий информацию о потенциальной угрозе.
     */
    @Data
    @AllArgsConstructor
    public static class ThreatInfo {
        /**
         * Типы угроз.
         */
        private String[] threatTypes;

        /**
         * Типы платформ.
         */
        private String[] platformTypes;

        /**
         * Типы записей о угрозах.
         */
        private String[] threatEntryTypes;

        /**
         * Записи о угрозах.
         */
        private ThreatEntry[] threatEntries;
    }

    /**
     * Класс, представляющий запись о угрозе.
     */
    @Data
    @AllArgsConstructor
    public static class ThreatEntry {
        /**
         * Ссылка на угрозу.
         */
        private String url;
    }
}
