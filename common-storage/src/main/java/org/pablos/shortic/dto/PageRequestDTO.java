package org.pablos.shortic.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PageRequestDTO {
    private final int page;
    private final int size;
    private final LinkUnitDTO linkUnit;
}
