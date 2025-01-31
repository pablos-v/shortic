package org.pablos.backendcountingservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.backendcountingservice.repository.ClickRepository;
import org.pablos.backendcountingservice.service.IClickService;
import org.pablos.backendcountingservice.service.ILinkUnitService;
import org.pablos.backendcountingservice.service.mapper.ClickMapper;
import org.pablos.common.dto.ClickDTO;
import org.pablos.common.exception.LinkNotFoundException;
import org.pablos.common.exception.LinkProcessingException;
import org.pablos.common.exception.ObjectNotProvidedException;
import org.pablos.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClickService implements IClickService {

    private final ClickRepository clickRepository;
    private final ILinkUnitService linkUnitService;

    @Override
    @Transactional
    public void createClick(ClickDTO dto) throws ObjectNotProvidedException, LinkNotFoundException, LinkProcessingException {
        CommonUtil.validateDTOShortLink(dto);

        Click click = ClickMapper.toEntity(dto);
        Long link_id = linkUnitService.getLinkUnitIdByShortLink(dto.getShortLink());
        click.setLinkId(link_id);

        clickRepository.save(click);
    }

}
