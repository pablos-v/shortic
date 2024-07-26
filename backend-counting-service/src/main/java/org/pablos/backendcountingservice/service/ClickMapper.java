package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.shortic.dto.ClickDTO;

public class ClickMapper {
    public static Click toEntity(ClickDTO dto) {
        Click click = new Click();
        click.setReferrer(dto.referer());
        click.setLanguage(dto.language());
        click.setUserAgent(dto.userAgent());
        click.setIpAddress(dto.ipAddress());
        return click;
    }

    public static ClickDTO toDTO(Click click) {
        return new ClickDTO(
                click.getId(),
                null,
                click.getClickTime(),
                click.getIpAddress(),
                click.getLanguage(),
                click.getReferrer(),
                click.getUserAgent());
    }
}
