package org.pablos.backendcountingservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiRequestBody {

    private Client client;
    private ThreatInfo threatInfo;

    public ApiRequestBody(String link) {
        this.client = new Client("shortic", "1.0");
        this.threatInfo = new ThreatInfo(
                new String[]{"MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE", "POTENTIALLY_HARMFUL_APPLICATION"},
                new String[]{"ALL_PLATFORMS"},
                new String[]{"URL", "EXECUTABLE"},
                new ThreatEntry[]{new ThreatEntry(link)}
        );
    }

    @Data
    @AllArgsConstructor
    public static class Client {
        private String clientId;
        private String clientVersion;
    }

    @Data
    @AllArgsConstructor
    public static class ThreatInfo {
        private String[] threatTypes;
        private String[] platformTypes;
        private String[] threatEntryTypes;
        private ThreatEntry[] threatEntries;
    }

    @Data
    @AllArgsConstructor
    public static class ThreatEntry {
        private String url;
    }
}
