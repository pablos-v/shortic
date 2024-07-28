package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;

import java.util.List;

public class LinkUnitMapper {
    public  static org.pablos.shortic.dto.LinkUnitDTO toDto(LinkUnit linkUnit) {
        return new org.pablos.shortic.dto.LinkUnitDTO(
                linkUnit.getId(),
                linkUnit.getShortLink(),
                linkUnit.getPassword(),
                linkUnit.getFullLink(),
                linkUnit.getCreatedAt(),
                linkUnit.isActive(),
                linkUnit.getClicks()
        );
    }
// TODO delete if not needed
    public static LinkUnit toEntity(org.pablos.shortic.dto.LinkUnitDTO linkUnitDTO) {
        return new LinkUnit(
                linkUnitDTO.getId(),
                linkUnitDTO.getShortLink(),
                linkUnitDTO.getPassword(),
                linkUnitDTO.getFullLink(),
                linkUnitDTO.getCreatedAt(),
                linkUnitDTO.isActive(),
                (List<Click>) linkUnitDTO.getClicks());
    }
}
