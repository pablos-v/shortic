package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.entity.LinkUnit;
import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.LinkNotFoundWhileActivationException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.common.dto.FastLinkDTO;
import org.pablos.common.dto.LinkUnitDTO;
import org.pablos.common.dto.PageDTO;
import org.pablos.common.exception.*;

import java.util.List;

public interface ILinkUnitService {
    /**
     * @param shortLink
     * @return
     * @throws LinkNotFoundException если ссылка не найдена
     */
    Long getLinkUnitIdByShortLink(String shortLink) throws LinkNotFoundException;

    LinkUnitDTO createLinkUnit(FastLinkDTO input) throws LinkNotFoundWhileActivationException, FullLinkSizeException,
            FullLinkFormatException, FullLinkNotProvidedException;

    /**
     * Получает из репозитория LinkUnit и формирует на основе PageRequestDTO страницу для пагинации.
     *
     * @return
     * @throws LinkNotFoundException
     * @throws WrongPasswordException
     */
    PageDTO getPage(int page, int size, String shortLink, String password) throws LinkNotFoundException,
            WrongPasswordException;

    /**
     * Проверяет безопасность новой ссылки. Активирует и сохраняет ее в БД.
     * Также запрашивает сохранение этой ссылки в backend-giving-service.
     *
     * @param linkUnit
     * @throws LinkNotFoundWhileActivationException
     * @throws SavingFastLinkException
     * @throws DeletingFastLinkException
     */
    void checkNewLinkSecurity(LinkUnitDTO dto) throws LinkNotFoundWhileActivationException, SavingFastLinkException
            , DeletingFastLinkException;

    /**
     * Проверяет безопасность существующей ссылки. Деактивирует ссылку и сохраняет в БД.
     * Также запрашивает удаление этой ссылки из backend-giving-service.
     *
     * @param linkUnit
     * @throws LinkNotFoundWhileActivationException
     * @throws SavingFastLinkException
     * @throws DeletingFastLinkException
     */
    boolean checkExistingLinkSecurity(LinkUnitDTO dto) throws LinkNotFoundWhileActivationException,
            SavingFastLinkException, DeletingFastLinkException;

    List<LinkUnit> getAllLinks();

    /**
     * Меняет полную ссылку у существующего LinkUnit. Проверяет безопасность новой ссылки.
     *
     * @return
     * @throws LinkNotSecureException
     */
    void updateLinkUnit(String shortLink, String fullLink) throws LinkNotSecureException, LinkNotFoundException,
            FullLinkNotProvidedException, FullLinkFormatException, FullLinkSizeException;

    void setPassword(String shortLink, String password) throws WrongInputException, LinkNotFoundException;
}
