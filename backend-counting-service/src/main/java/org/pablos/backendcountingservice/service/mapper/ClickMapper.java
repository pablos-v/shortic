package org.pablos.backendcountingservice.service.mapper;

import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.common.dto.ClickDTO;

public class ClickMapper {
    public static Click toEntity(ClickDTO dto) {
        return new Click(
                dto.getClickTime(),
                dto.getIpAddress(),
                dto.getUserAgent(),
                dto.getReferer(),
                dto.getLanguage());
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
