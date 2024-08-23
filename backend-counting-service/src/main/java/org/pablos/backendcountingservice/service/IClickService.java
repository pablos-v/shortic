package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.entity.Click;
import org.pablos.common.dto.ClickDTO;
import org.pablos.common.exception.LinkNotFoundException;
import org.pablos.common.exception.LinkProcessingException;
import org.pablos.common.exception.ObjectNotProvidedException;

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
