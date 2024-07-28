package org.pablos.backendgivingservice.service;

import org.pablos.shortic.dto.ClickDTO;

public interface ICountingServiceClient {
    /**
     * TODO
     * @param click
     */
    void postStatistics(final ClickDTO click);
}
