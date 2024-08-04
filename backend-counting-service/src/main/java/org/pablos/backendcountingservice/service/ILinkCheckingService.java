package org.pablos.backendcountingservice.service;

import org.pablos.shortic.dto.FastLinkDTO;

public interface ILinkCheckingService {
    boolean checkLink(final String link);
}
