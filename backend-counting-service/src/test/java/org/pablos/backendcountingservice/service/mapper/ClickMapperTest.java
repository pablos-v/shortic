package org.pablos.backendcountingservice.service.mapper;

import org.junit.jupiter.api.Test;
import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.common.dto.ClickDTO;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ClickMapperTest {

    @Test
    public void testToEntity() {
        ClickDTO dto = new ClickDTO(-1, "http://example.com", LocalDateTime.now(),
                "127.0.0.1", "en", "some", "some");

        Click click = ClickMapper.toEntity(dto);

        assertThat(click.getClickTime()).isEqualTo(dto.getClickTime());
        assertThat(click.getIpAddress()).isEqualTo(dto.getIpAddress());
        assertThat(click.getUserAgent()).isEqualTo(dto.getUserAgent());
        assertThat(click.getReferrer()).isEqualTo(dto.getReferer());
        assertThat(click.getLanguage()).isEqualTo(dto.getLanguage());
    }

    @Test
    public void testToDTO() {
        Click click = new Click();
        click.setId(1L);
        click.setClickTime(LocalDateTime.now());
        click.setIpAddress("127.0.0.1");
        click.setUserAgent("Mozilla/5.0");
        click.setReferrer("http://example.com");
        click.setLanguage("en");

        ClickDTO dto = ClickMapper.toDTO(click);

        assertThat(dto.getId()).isEqualTo(click.getId());
        assertThat(dto.getShortLink()).isNull();
        assertThat(dto.getClickTime()).isEqualTo(click.getClickTime());
        assertThat(dto.getIpAddress()).isEqualTo(click.getIpAddress());
        assertThat(dto.getLanguage()).isEqualTo(click.getLanguage());
        assertThat(dto.getReferer()).isEqualTo(click.getReferrer());
        assertThat(dto.getUserAgent()).isEqualTo(click.getUserAgent());
    }

}


