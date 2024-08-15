package org.pablos.shortic.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.shortic.IShortLink;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Data
@RequiredArgsConstructor
public final class ClickDTO implements IShortLink {

    private final long id;
    private final String shortLink;
    private final LocalDateTime clickTime;
    private final String ipAddress;
    private final String language;
    private final String referer;
    private final String userAgent;

    /**
     * Форматирование времени для удобного отображения. Используется шаблоном Thymeleaf statistics.html
     * @return Строку с форматированным временем
     */
    public String getFormattedTime(){
        return clickTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
