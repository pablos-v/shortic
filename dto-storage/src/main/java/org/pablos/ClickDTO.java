package org.pablos;

import java.time.LocalDateTime;

public record ClickDTO(long id, LocalDateTime clickTime, String ipAddress, String language, String referer, String userAgent) {
}
