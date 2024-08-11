package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.shortic.dto.LinkUnitDTO;

import java.util.ArrayList;
import java.util.List;

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
// TODO delete if not needed
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

    public static List<LinkUnit> toEntityList(List<LinkUnitDTO> linkUnitDTOList) {
        return linkUnitDTOList.stream().map(LinkUnitMapper::toEntity).toList();
    }

    public static List<LinkUnitDTO> toDTOList(List<LinkUnit> linkUnitList) {
        return linkUnitList.stream().map(LinkUnitMapper::toDto).toList();
    }
}
