package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.common.dto.FastLinkDTO;

/**
 * Интерфейс сервиса для работы с быстрыми ссылками.
 */
public interface IGivingServiceClient {

    /**
     * Сохраняет быструю ссылку.
     *
     * @param dto DTO быстрой ссылки.
     * @return сохраненная быстрая ссылка.
     * @throws SavingFastLinkException если возникла ошибка при сохранении быстрой ссылки.
     */
    FastLinkDTO saveFastLink(final FastLinkDTO dto) throws SavingFastLinkException;

    /**
     * Удаляет быструю ссылку.
     *
     * @param dto DTO быстрой ссылки.
     * @return удаленная быстрая ссылка.
     * @throws DeletingFastLinkException если возникла ошибка при удалении быстрой ссылки.
     */
    FastLinkDTO deleteFastLink(final FastLinkDTO dto) throws DeletingFastLinkException;

    /**
     * Обновляет быструю ссылку.
     *
     * @param fastLinkDTO DTO быстрой ссылки.
     */
    void updateFastLink(final FastLinkDTO fastLinkDTO);
}
