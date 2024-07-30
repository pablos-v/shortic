package org.pablos.backendcountingservice.domain.dto;

import lombok.Data;

@Data
public class Match {
    private String threatType;
    private String threatEntryType;
    private String platformType;
    private Threat threat;
    private String cacheDuration;
    @Data
    public static class Threat {
        private String url;
    }
}