package org.pablos.backendgivingservice.service;

import org.junit.jupiter.api.Test;
import org.pablos.backendgivingservice.entity.FastLink;
import org.pablos.backendgivingservice.service.FastLinkMapper;
import org.pablos.common.dto.FastLinkDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FastLinkMapperTest {

    @Test
    public void testToDTO() {
        FastLink fastLink = new FastLink("shortLink", "fullLink");
        FastLinkDTO dto = FastLinkMapper.toDTO(fastLink);

        assertEquals(fastLink.getShortLink(), dto.getShortLink());
        assertEquals(fastLink.getFullLink(), dto.getFullLink());
    }

    @Test
    public void testToEntity() {
        FastLinkDTO dto = new FastLinkDTO("shortLink", "fullLink");
        FastLink fastLink = FastLinkMapper.toEntity(dto);

        assertEquals(dto.getShortLink(), fastLink.getShortLink());
        assertEquals(dto.getFullLink(), fastLink.getFullLink());
    }

}



