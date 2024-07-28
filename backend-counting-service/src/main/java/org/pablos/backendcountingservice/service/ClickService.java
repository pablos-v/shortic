package org.pablos.backendcountingservice.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.shortic.dto.ClickDTO;
import org.pablos.backendcountingservice.repository.ClickRepository;
import org.pablos.shortic.exception.ObjectNotProvidedException;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Service
public class ClickService {

    private final ClickRepository clickRepository;
    private final LinkUnitService linkUnitService;

    public void createClick(ClickDTO dto) {
        if (dto == null) throw new ObjectNotProvidedException();
        CommonUtil.validateShortLink(dto.shortLink());

        Click click = ClickMapper.toEntity(dto);
        click.setLinkId(linkUnitService.getLinkUnitByShortLink(dto.shortLink()).id());
        click.setClickTime(LocalDateTime.now());

        clickRepository.save(click);
    }

}
