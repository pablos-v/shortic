package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.shortic.dto.LinkUnitDTO;

import java.util.List;

public class LinkUnitMapper {
    public  static LinkUnitDTO toDto(LinkUnit linkUnit) {
        return new LinkUnitDTO(
                linkUnit.getId(),
                linkUnit.getShortLink(),
                linkUnit.getPassword(),
                linkUnit.getFullLink(),
                linkUnit.getCreatedAt(),
                linkUnit.isStatus(),
                linkUnit.getClicks()
        );
    }

    public static LinkUnit toEntity(LinkUnitDTO linkUnitDTO) {
        return new LinkUnit(
                linkUnitDTO.id(),
                linkUnitDTO.shortLink(),
                linkUnitDTO.password(),
                linkUnitDTO.fullLink(),
                linkUnitDTO.createdAt(),
                linkUnitDTO.status(),
                (List<Click>) linkUnitDTO.clicks());
    }
}
