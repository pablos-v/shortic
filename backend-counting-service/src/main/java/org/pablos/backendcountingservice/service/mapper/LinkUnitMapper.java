package org.pablos.backendcountingservice.service.mapper;

import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.common.dto.LinkUnitDTO;

import java.util.ArrayList;

public class LinkUnitMapper {
    public  static LinkUnitDTO toDto(LinkUnit linkUnit) {
        return new LinkUnitDTO(
                linkUnit.getId(),
                linkUnit.getShortLink(),
                linkUnit.getPassword(),
                linkUnit.getFullLink(),
                linkUnit.getCreatedAt(),
                linkUnit.isActive()
        );
    }
    public static LinkUnit toEntity(LinkUnitDTO linkUnitDTO) {
        return new LinkUnit(
                linkUnitDTO.getId(),
                linkUnitDTO.getShortLink(),
                linkUnitDTO.getPassword(),
                linkUnitDTO.getFullLink(),
                linkUnitDTO.getCreatedAt(),
                linkUnitDTO.isActive(),
                new ArrayList<>()
        );
    }
}
