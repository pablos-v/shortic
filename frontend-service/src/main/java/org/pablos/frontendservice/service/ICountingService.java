package org.pablos.frontendservice.service;

import org.pablos.shortic.dto.ClickDTO;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.dto.LinkUnitDTO;
import org.pablos.shortic.dto.PageDTO;
import org.pablos.shortic.exception.*;

public interface ICountingService {
    /**
     * Посылает запрос на создание ссылки.
     *
     * @param input
     * @return
     */
    LinkUnitDTO createLink(FastLinkDTO input) throws WrongInputException, FullLinkNotProvidedException,
            FullLinkSizeException, FullLinkFormatException;

    PageDTO getPageOfClicks(int page, int size, String shortLink, String password) throws WrongInputException,
            WrongPasswordException, LinkNotFoundException;

    void postStatistics(ClickDTO clickDTO);

    void updateLink(String shortLink, String fullLink) throws LinkNotSecureException, WrongInputException,
            FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException;

    void setPassword(String shortLink, String password);
}
