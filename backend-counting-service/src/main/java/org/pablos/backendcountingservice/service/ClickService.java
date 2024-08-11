package org.pablos.backendcountingservice.service;

import lombok.RequiredArgsConstructor;
import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.backendcountingservice.repository.ClickRepository;
import org.pablos.shortic.dto.ClickDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.exception.ObjectNotProvidedException;
import org.pablos.shortic.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Data
@RequiredArgsConstructor
@Service
public class ClickService {

    private final ClickRepository clickRepository;
    private final LinkUnitService linkUnitService;

    /**
     * Метод создания и записи в БД объекта {@link Click}. В процессе валидирует ссылку.
     *
     * @param dto DTO с параметрами клика
     * @throws ObjectNotProvidedException если объект не был передан
     * @throws LinkNotFoundException если ссылка не найдена
     * @throws LinkProcessingException если ссылка не прошла валидацию
     */
    @Transactional
    public void createClick(ClickDTO dto) throws ObjectNotProvidedException, LinkNotFoundException, LinkProcessingException {
        CommonUtil.validateDTOShortLink(dto);

        Click click = ClickMapper.toEntity(dto);
        Long link_id = linkUnitService.getLinkUnitIdByShortLink(dto.getShortLink());
        click.setLinkId(link_id);

        clickRepository.save(click);
    }
//
//    /**
//     * Метод получения страницы пагинации по ID ссылки.
//     * @param linkId
//     * @param pageable
//     * @return
//     */
//    @Transactional
//    public Page<ClickDTO> getPageClicksByLinkId(Long linkId, Pageable pageable) {
//        Page<Click> clicks = clickRepository.findAllByLinkId(linkId, pageable);
//        return clicks.map(ClickMapper::toDTO);
//    }

}
