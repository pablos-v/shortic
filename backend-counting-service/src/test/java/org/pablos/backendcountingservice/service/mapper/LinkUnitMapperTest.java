package org.pablos.backendcountingservice.service.mapper;

import org.junit.jupiter.api.Test;
import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.service.mapper.LinkUnitMapper;
import org.pablos.common.dto.LinkUnitDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkUnitMapperTest {

    @Test
    public void testToDto() {
        LinkUnit linkUnit = new LinkUnit(1L, "shortLink", "password", "fullLink", LocalDateTime.now(), true, new ArrayList<>());
        LinkUnitDTO dto = LinkUnitMapper.toDto(linkUnit);

        assertThat(dto.getId()).isEqualTo(linkUnit.getId());
        assertThat(dto.getShortLink()).isEqualTo(linkUnit.getShortLink());
        assertThat(dto.getPassword()).isEqualTo(linkUnit.getPassword());
        assertThat(dto.getFullLink()).isEqualTo(linkUnit.getFullLink());
        assertThat(dto.getCreatedAt()).isEqualTo(linkUnit.getCreatedAt());
        assertThat(dto.isActive()).isEqualTo(linkUnit.isActive());
    }

    @Test
    public void testToEntity() {
        LinkUnitDTO dto = new LinkUnitDTO(1L, "shortLink", "password", "fullLink", LocalDateTime.now(), true);
        LinkUnit linkUnit = LinkUnitMapper.toEntity(dto);

        assertThat(linkUnit.getId()).isEqualTo(dto.getId());
        assertThat(linkUnit.getShortLink()).isEqualTo(dto.getShortLink());
        assertThat(linkUnit.getPassword()).isEqualTo(dto.getPassword());
        assertThat(linkUnit.getFullLink()).isEqualTo(dto.getFullLink());
        assertThat(linkUnit.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(linkUnit.isActive()).isEqualTo(dto.isActive());
    }

}



