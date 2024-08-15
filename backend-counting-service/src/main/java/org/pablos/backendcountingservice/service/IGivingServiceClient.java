package org.pablos.backendcountingservice.service;

import org.pablos.backendcountingservice.domain.exception.DeletingFastLinkException;
import org.pablos.backendcountingservice.domain.exception.SavingFastLinkException;
import org.pablos.shortic.dto.FastLinkDTO;

public interface IGivingServiceClient {
    /**
     * TODO
     *
     * @param link
     */
    FastLinkDTO saveFastLink(final FastLinkDTO dto) throws SavingFastLinkException;

    FastLinkDTO deleteFastLink(final FastLinkDTO dto) throws DeletingFastLinkException;

    void updateFastLink(final FastLinkDTO fastLinkDTO);
}
