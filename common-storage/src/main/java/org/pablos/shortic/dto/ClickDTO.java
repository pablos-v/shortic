package org.pablos.shortic.dto;

import java.time.LocalDateTime;

public record ClickDTO(long id, String shortLink, LocalDateTime clickTime, String ipAddress, String language, String referer, String userAgent) {
}
