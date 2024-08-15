package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.shortic.dto.ClickDTO;
import org.pablos.shortic.exception.LinkNotFoundException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.exception.ObjectNotProvidedException;
import org.springframework.transaction.annotation.Transactional;

public interface IClickService {
    /**
     * Метод создания и записи в БД объекта {@link Click}. В процессе валидирует ссылку.
     *
     * @param dto DTO с параметрами клика
     * @throws ObjectNotProvidedException если объект не был передан
     * @throws LinkNotFoundException      если ссылка не найдена
     * @throws LinkProcessingException    если ссылка не прошла валидацию
     */
    void createClick(ClickDTO dto) throws ObjectNotProvidedException, LinkNotFoundException, LinkProcessingException;
}
